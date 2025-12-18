package com.example.hotelbookingapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hotelbookingapp.R;
import com.example.hotelbookingapp.activities.NotificationActivity;
import com.example.hotelbookingapp.adapters.HomeAdapter;
import com.example.hotelbookingapp.models.Hotel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class HomeFragment extends Fragment {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private EditText etLocation;
    private LinearLayout btnFilter;
    private RecyclerView rvHotels;
    private HomeAdapter hotelAdapter;
    private List<Hotel> hotelList;
    private List<Hotel> originalHotelList;
    private ImageView imgAvatar;
    private TextView tvGreeting;

    // --- KHAI BÁO CÁC NÚT ĐỊA ĐIỂM ---
    private LinearLayout layoutNearMe, layoutHanoi, layoutDanang, layoutHCM, layoutDaLat, layoutVungTau;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Ánh xạ View chính
        etLocation = view.findViewById(R.id.etLocation);
        btnFilter = view.findViewById(R.id.btnFilter);
        rvHotels = view.findViewById(R.id.rvHotels);
        imgAvatar = view.findViewById(R.id.imgAvatar);
        tvGreeting = view.findViewById(R.id.tvGreeting);
        ImageView btnNotification = view.findViewById(R.id.btn_notification);

        // Ánh xạ các địa điểm khám phá
        layoutNearMe = view.findViewById(R.id.layoutNearMe);
        layoutHanoi = view.findViewById(R.id.layoutHanoi);
        layoutDanang = view.findViewById(R.id.layoutDanang);
        layoutHCM = view.findViewById(R.id.layoutHCM);
        layoutDaLat = view.findViewById(R.id.layoutDaLat);
        layoutVungTau = view.findViewById(R.id.layoutVungTau);

        setupRecyclerView();
        loadDefaultHotels();
        loadUserProfile();
        setupCityClickListeners();

        // SEARCH REAL-TIME (KHÔNG DẤU)
        etLocation.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                performLocalSearch(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        etLocation.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performLocalSearch(etLocation.getText().toString());
                return true;
            }
            return false;
        });

        btnFilter.setOnClickListener(v -> showFilterDialog());

        if (btnNotification != null) {
            btnNotification.setOnClickListener(v -> {
                if (getContext() != null) {
                    startActivity(new Intent(getContext(), NotificationActivity.class));
                }
            });
        }

        return view;
    }

    // LOGIC CLICK ĐỊA ĐIỂM
    private void setupCityClickListeners() {
        if (layoutHanoi != null) layoutHanoi.setOnClickListener(v -> { etLocation.setText("Hà Nội"); performLocalSearch("Hà Nội"); });
        if (layoutDanang != null) layoutDanang.setOnClickListener(v -> { etLocation.setText("Đà Nẵng"); performLocalSearch("Đà Nẵng"); });
        if (layoutHCM != null) layoutHCM.setOnClickListener(v -> { etLocation.setText("Hồ Chí Minh"); performLocalSearch("Hồ Chí Minh"); });
        if (layoutDaLat != null) layoutDaLat.setOnClickListener(v -> { etLocation.setText("Đà Lạt"); performLocalSearch("Đà Lạt"); });
        if (layoutVungTau != null) layoutVungTau.setOnClickListener(v -> { etLocation.setText("Vũng Tàu"); performLocalSearch("Vũng Tàu"); });

        // Nút Gần đây hiện tại đóng vai trò Reset (Show All)
        if (layoutNearMe != null) layoutNearMe.setOnClickListener(v -> {
            etLocation.setText("");
            performLocalSearch("");
            Toast.makeText(getContext(), "Đang hiển thị tất cả khách sạn", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserProfile();
    }

    private void loadUserProfile() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            db.collection("users").document(user.getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (getContext() == null || !isAdded()) return;
                        if (documentSnapshot.exists()) {
                            String fullname = documentSnapshot.getString("fullname");
                            tvGreeting.setText(fullname != null && !fullname.isEmpty() ? "Xin chào, " + fullname + "!" : "Xin chào!");
                            String avatarUrl = documentSnapshot.getString("avatarUrl");
                            if (avatarUrl != null && !avatarUrl.isEmpty()) {
                                Glide.with(this).load(avatarUrl).circleCrop().into(imgAvatar);
                            }
                        }
                    });
        }
    }

    private void setupRecyclerView() {
        hotelList = new ArrayList<>();
        originalHotelList = new ArrayList<>();
        hotelAdapter = new HomeAdapter(getContext(), hotelList);
        rvHotels.setLayoutManager(new LinearLayoutManager(getContext()));
        rvHotels.setAdapter(hotelAdapter);
    }

    private void loadDefaultHotels() {
        db.collection("hotels").orderBy("name").get()
                .addOnSuccessListener(snapshots -> {
                    hotelList.clear();
                    originalHotelList.clear();
                    for (QueryDocumentSnapshot doc : snapshots) {
                        Hotel hotel = doc.toObject(Hotel.class);
                        hotel.setId(doc.getId());
                        hotelList.add(hotel);
                        originalHotelList.add(hotel);
                    }
                    hotelAdapter.notifyDataSetChanged();
                });
    }

    // HÀM SEARCH CORE (PHAN ANH)
    private void performLocalSearch(String text) {
        if (originalHotelList == null) return;
        List<Hotel> filteredList = new ArrayList<>();
        String query = removeAccent(text.toLowerCase().trim());

        if (query.isEmpty()) {
            filteredList.addAll(originalHotelList);
        } else {
            for (Hotel hotel : originalHotelList) {
                String name = (hotel.getName() != null) ? removeAccent(hotel.getName().toLowerCase()) : "";
                String loc = (hotel.getLocation() != null) ? removeAccent(hotel.getLocation().toLowerCase()) : "";
                if (name.contains(query) || loc.contains(query)) {
                    filteredList.add(hotel);
                }
            }
        }
        hotelList.clear();
        hotelList.addAll(filteredList);
        hotelAdapter.notifyDataSetChanged();
    }

    // LOGIC BỘ LỌC
    private void showFilterDialog() {
        if (getContext() == null) return;
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        dialog.setContentView(R.layout.dialog_filter);

        // Ánh xạ 7 CheckBox
        CheckBox cbWifi = dialog.findViewById(R.id.cbWifi);
        CheckBox cbPool = dialog.findViewById(R.id.cbPool);
        CheckBox cbAC = dialog.findViewById(R.id.cbAC); // Khớp ID cbAC
        CheckBox cbParking = dialog.findViewById(R.id.cbParking);
        CheckBox cbGym = dialog.findViewById(R.id.cbGym);
        CheckBox cbSpa = dialog.findViewById(R.id.cbSpa);
        CheckBox cbBar = dialog.findViewById(R.id.cbBar);
        Button btnApply = dialog.findViewById(R.id.btnApplyFilter);

        if (btnApply != null) {
            btnApply.setOnClickListener(v -> {
                List<String> selectedAmenities = new ArrayList<>();
                if (cbWifi != null && cbWifi.isChecked()) selectedAmenities.add("wifi");
                if (cbPool != null && cbPool.isChecked()) selectedAmenities.add("pool");
                if (cbAC != null && cbAC.isChecked()) selectedAmenities.add("ac");
                if (cbParking != null && cbParking.isChecked()) selectedAmenities.add("parking");
                if (cbGym != null && cbGym.isChecked()) selectedAmenities.add("gym");
                if (cbSpa != null && cbSpa.isChecked()) selectedAmenities.add("spa");
                if (cbBar != null && cbBar.isChecked()) selectedAmenities.add("bar");

                Log.e("CHECK_FILTER", "Phan Anh chọn: " + selectedAmenities.toString());
                applyFilter(selectedAmenities);
                dialog.dismiss();
            });
        }
        dialog.show();
    }

    private void applyFilter(List<String> requiredAmenities) {
        if (requiredAmenities == null || requiredAmenities.isEmpty()) {
            hotelList.clear();
            hotelList.addAll(originalHotelList);
            hotelAdapter.notifyDataSetChanged();
            return;
        }
        List<Hotel> filteredList = new ArrayList<>();
        List<String> reqLower = new ArrayList<>();
        for (String req : requiredAmenities) reqLower.add(req.toLowerCase().trim());

        for (Hotel hotel : originalHotelList) {
            List<String> hotelAmenities = hotel.getAmenities();
            if (hotelAmenities != null) {
                List<String> hotelAmenitiesLower = new ArrayList<>();
                for (String a : hotelAmenities) hotelAmenitiesLower.add(a.toLowerCase().trim());
                if (hotelAmenitiesLower.containsAll(reqLower)) filteredList.add(hotel);
            }
        }
        hotelList.clear();
        hotelList.addAll(filteredList);
        hotelAdapter.notifyDataSetChanged();

        //Thông báo nếu không lọc filter không có kết quả
        if (filteredList.isEmpty() && getContext() != null) {
            Toast.makeText(getContext(), "Không tìm thấy khách sạn phù hợp với bộ lọc", Toast.LENGTH_SHORT).show();
        }
    }

    private String removeAccent(String s) {
        if (s == null) return "";
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replace('đ', 'd').replace('Đ', 'D');
    }
}