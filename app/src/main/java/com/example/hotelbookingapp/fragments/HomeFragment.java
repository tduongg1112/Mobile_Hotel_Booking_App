package com.example.hotelbookingapp.fragments;

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
    private List<Hotel> originalHotelList; // Danh sách gốc để lọc (PhanAnh Core)
    private ImageView imgAvatar;
    private TextView tvGreeting;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Khởi tạo các biến hệ thống (Từ Main)
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Ánh xạ View (Kết hợp cả hai bên)
        etLocation = view.findViewById(R.id.etLocation);
        btnFilter = view.findViewById(R.id.btnFilter);
        rvHotels = view.findViewById(R.id.rvHotels);
        imgAvatar = view.findViewById(R.id.imgAvatar);
        tvGreeting = view.findViewById(R.id.tvGreeting);
        ImageView btnNotification = view.findViewById(R.id.btn_notification);

        setupRecyclerView();
        loadDefaultHotels();
        loadUserProfile();

        // LOGIC SEARCH (PhanAnh Core): Tìm kiếm không dấu + Real-time
        etLocation.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                performLocalSearch(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // Hỗ trợ nhấn nút Search trên bàn phím
        etLocation.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performLocalSearch(etLocation.getText().toString());
                return true;
            }
            return false;
        });

        btnFilter.setOnClickListener(v -> showFilterDialog());

        // Logic Notification (Từ Main)
        if (btnNotification != null) {
            btnNotification.setOnClickListener(v -> {
                if (getContext() != null) {
                    android.content.Intent intent = new android.content.Intent(getContext(), com.example.hotelbookingapp.activities.NotificationActivity.class);
                    startActivity(intent);
                }
            });
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserProfile();
    }

    // Tải thông tin người dùng (Từ Main)
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
                            } else {
                                imgAvatar.setImageResource(R.drawable.ic_user);
                            }
                        }
                    })
                    .addOnFailureListener(e -> Log.e("HomeFragment", "Error loading user profile", e));
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
                })
                .addOnFailureListener(e -> {
                    if (getContext() != null)
                        Toast.makeText(getContext(), "Lỗi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // HÀM SEARCH TỐI ƯU (PhanAnh Core): Lọc trực tiếp Name và Location không dấu
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

    private void showFilterDialog() {
        if (getContext() == null) return;
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        dialog.setContentView(R.layout.dialog_filter);

        CheckBox cbWifi = dialog.findViewById(R.id.cbWifi);
        CheckBox cbPool = dialog.findViewById(R.id.cbPool);
        Button btnApply = dialog.findViewById(R.id.btnApplyFilter);

        if (btnApply != null) btnApply.setOnClickListener(v -> {
            List<String> selectedAmenities = new ArrayList<>();
            if (cbWifi != null && cbWifi.isChecked()) selectedAmenities.add("wifi");
            if (cbPool != null && cbPool.isChecked()) selectedAmenities.add("pool");
            applyFilter(selectedAmenities);
            dialog.dismiss();
        });
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
                if (hotelAmenitiesLower.containsAll(reqLower)) {
                    filteredList.add(hotel);
                }
            }
        }
        hotelList.clear();
        hotelList.addAll(filteredList);
        hotelAdapter.notifyDataSetChanged();
    }

    // Hàm bỏ dấu (PhanAnh Core)
    private String removeAccent(String s) {
        if (s == null) return "";
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replace('đ', 'd').replace('Đ', 'D');
    }
}