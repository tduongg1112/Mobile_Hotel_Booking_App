package com.example.hotelbookingapp.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log; // Import thêm thư viện Log
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelbookingapp.R;
import com.example.hotelbookingapp.adapters.HomeAdapter;
import com.example.hotelbookingapp.models.Hotel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ExploreFragment extends Fragment {
    private RecyclerView rvExplore;
    private HomeAdapter adapter;
    private List<Hotel> exploreList;
    private ImageView btnFilter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        rvExplore = view.findViewById(R.id.rvExplore);
        // Đảm bảo ID này đúng với file fragment_explore.xml
        btnFilter = view.findViewById(R.id.btnFilter);

        exploreList = new ArrayList<>();
        adapter = new HomeAdapter(getContext(), exploreList);
        rvExplore.setLayoutManager(new LinearLayoutManager(getContext()));
        rvExplore.setAdapter(adapter);

        loadTopRatedHotels();

        if (btnFilter != null) {
            btnFilter.setOnClickListener(v -> showFilterDialog());
        }

        return view;
    }

    private void showFilterDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_filter);

        CheckBox cbWifi = dialog.findViewById(R.id.cbWifi);
        CheckBox cbPool = dialog.findViewById(R.id.cbPool);
        CheckBox cbAC = dialog.findViewById(R.id.cbAC);
        CheckBox cbParking = dialog.findViewById(R.id.cbParking);
        CheckBox cbGym = dialog.findViewById(R.id.cbGym);
        CheckBox cbSpa = dialog.findViewById(R.id.cbSpa);
        CheckBox cbBar = dialog.findViewById(R.id.cbBar);

        ImageView btnClose = dialog.findViewById(R.id.btnCloseFilter);
        Button btnApply = dialog.findViewById(R.id.btnApplyFilter);

        btnClose.setOnClickListener(v -> dialog.dismiss());

        btnApply.setOnClickListener(v -> {
            List<String> selectedAmenities = new ArrayList<>();

            // --- SỬA QUAN TRỌNG: ĐỔI THÀNH CHỮ IN HOA ĐỂ KHỚP FIREBASE ---
            if (cbWifi.isChecked()) selectedAmenities.add("WIFI");
            if (cbPool.isChecked()) selectedAmenities.add("POOL");
            if (cbAC.isChecked()) selectedAmenities.add("AC");
            if (cbParking.isChecked()) selectedAmenities.add("PARKING");
            if (cbGym.isChecked()) selectedAmenities.add("GYM");
            if (cbSpa.isChecked()) selectedAmenities.add("SPA");
            if (cbBar.isChecked()) selectedAmenities.add("BAR");
            // -------------------------------------------------------------

            Log.e("CHECK_FILTER", "Người dùng chọn: " + selectedAmenities.toString());

            filterHotelsByAmenities(selectedAmenities);
            dialog.dismiss();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void filterHotelsByAmenities(List<String> requiredAmenities) {
        if (requiredAmenities.isEmpty()) {
            loadTopRatedHotels();
            return;
        }

        FirebaseFirestore.getInstance().collection("hotels")
                .get()
                .addOnSuccessListener(snapshots -> {
                    exploreList.clear();
                    Log.e("CHECK_FILTER", "Đã tải về tổng cộng: " + snapshots.size() + " khách sạn để kiểm tra.");

                    for (QueryDocumentSnapshot doc : snapshots) {
                        Hotel hotel = doc.toObject(Hotel.class);
                        hotel.setId(doc.getId());

                        // Lấy danh sách tiện ích của khách sạn (đảm bảo không null)
                        List<String> hotelAmenities = hotel.getAmenities();
                        if (hotelAmenities == null) hotelAmenities = new ArrayList<>();

                        Log.e("CHECK_FILTER", "Khách sạn: " + hotel.getName() + " | Có: " + hotelAmenities.toString());

                        // Kiểm tra: Khách sạn có chứa TẤT CẢ cái mình chọn không?
                        // Sử dụng logic linh hoạt hơn chút:
                        // Nếu mình chọn WIFI và POOL, khách sạn phải có cả 2 mới hiện.
                        if (hotelAmenities.containsAll(requiredAmenities)) {
                            exploreList.add(hotel);
                            Log.e("CHECK_FILTER", "--> ĐƯỢC CHỌN");
                        } else {
                            Log.e("CHECK_FILTER", "--> BỊ LOẠI");
                        }
                    }
                    adapter.notifyDataSetChanged();

                    if (exploreList.isEmpty()) {
                        Toast.makeText(getContext(), "Không tìm thấy khách sạn nào!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("CHECK_FILTER", "Lỗi tải dữ liệu: " + e.getMessage());
                });
    }

    private void loadTopRatedHotels() {
        FirebaseFirestore.getInstance().collection("hotels")
                .whereGreaterThanOrEqualTo("ratingAverage", 4.5)
                .orderBy("ratingAverage", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(snapshots -> {
                    exploreList.clear();
                    for (QueryDocumentSnapshot doc : snapshots) {
                        Hotel hotel = doc.toObject(Hotel.class);
                        hotel.setId(doc.getId());
                        exploreList.add(hotel);
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}