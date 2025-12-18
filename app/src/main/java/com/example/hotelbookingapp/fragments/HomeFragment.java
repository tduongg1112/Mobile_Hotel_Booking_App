package com.example.hotelbookingapp.fragments;

import android.os.Bundle;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelbookingapp.R;
import com.example.hotelbookingapp.adapters.HomeAdapter;
import com.example.hotelbookingapp.models.Hotel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FirebaseFirestore db;
    private EditText etLocation;
    private LinearLayout btnFilter;
    private RecyclerView rvHotels;
    private HomeAdapter hotelAdapter;
    private List<Hotel> hotelList;
    private List<Hotel> originalHotelList; // Danh sách gốc để lọc

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        db = FirebaseFirestore.getInstance();
        etLocation = view.findViewById(R.id.etLocation);
        btnFilter = view.findViewById(R.id.btnFilter);
        rvHotels = view.findViewById(R.id.rvHotels);
        ImageView btnNotification = view.findViewById(R.id.btn_notification);

        setupRecyclerView();
        loadDefaultHotels();

        // Xử lý sự kiện khi nhấn Enter trên bàn phím để tìm kiếm
        etLocation.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });

        btnFilter.setOnClickListener(v -> showFilterDialog());

        btnNotification.setOnClickListener(v -> {
            if (getContext() != null) {
                android.content.Intent intent = new android.content.Intent(getContext(), com.example.hotelbookingapp.activities.NotificationActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void setupRecyclerView() {
        hotelList = new ArrayList<>();
        originalHotelList = new ArrayList<>();
        hotelAdapter = new HomeAdapter(getContext(), hotelList);
        rvHotels.setLayoutManager(new LinearLayoutManager(getContext()));
        rvHotels.setAdapter(hotelAdapter);
    }

    private void loadDefaultHotels() {
        // Sắp xếp theo tên để danh sách đẹp hơn
        db.collection("hotels").orderBy("name").get()
                .addOnSuccessListener(snapshots -> {
                    hotelList.clear();
                    originalHotelList.clear(); // Xóa danh sách gốc cũ

                    for (QueryDocumentSnapshot doc : snapshots) {
                        Hotel hotel = doc.toObject(Hotel.class);
                        hotel.setId(doc.getId());

                        hotelList.add(hotel);
                        originalHotelList.add(hotel); // QUAN TRỌNG: Lưu bản sao để dùng cho bộ lọc
                    }
                    hotelAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    if (getContext() != null)
                        Toast.makeText(getContext(), "Lỗi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void performSearch() {
        String keyword = etLocation.getText().toString().trim().toLowerCase();

        // Nếu từ khóa rỗng thì tải lại mặc định
        if (keyword.isEmpty()) {
            loadDefaultHotels();
            return;
        }

        db.collection("hotels")
                .whereArrayContains("searchKeywords", keyword)
                .get()
                .addOnSuccessListener(snapshots -> {
                    hotelList.clear();
                    originalHotelList.clear(); // Reset danh sách gốc theo kết quả tìm kiếm mới

                    for (QueryDocumentSnapshot doc : snapshots) {
                        Hotel hotel = doc.toObject(Hotel.class);
                        hotel.setId(doc.getId());

                        hotelList.add(hotel);
                        originalHotelList.add(hotel); // Lưu lại kết quả tìm kiếm để lọc trên kết quả này
                    }
                    hotelAdapter.notifyDataSetChanged();

                    if (hotelList.isEmpty() && getContext() != null) {
                        Toast.makeText(getContext(), "Không tìm thấy kết quả", Toast.LENGTH_SHORT).show();
                    }
                });
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
            // Truyền từ khóa (không quan trọng hoa thường vì hàm applyFilter sẽ xử lý)
            if (cbWifi != null && cbWifi.isChecked()) selectedAmenities.add("wifi");
            if (cbPool != null && cbPool.isChecked()) selectedAmenities.add("pool");

            applyFilter(selectedAmenities);
            dialog.dismiss();
        });
        dialog.show();
    }

    // --- HÀM LỌC ĐÃ ĐƯỢC SỬA LẠI (QUAN TRỌNG NHẤT) ---
    private void applyFilter(List<String> requiredAmenities) {
        // 1. Nếu không chọn gì -> Trả về danh sách gốc (originalHotelList)
        if (requiredAmenities == null || requiredAmenities.isEmpty()) {
            hotelList.clear();
            if (originalHotelList != null) {
                hotelList.addAll(originalHotelList);
            }
            hotelAdapter.notifyDataSetChanged();
            return;
        }

        List<Hotel> filteredList = new ArrayList<>();

        // 2. Chuyển các yêu cầu lọc về chữ thường (Ví dụ: "WIFI" -> "wifi")
        List<String> reqLower = new ArrayList<>();
        for (String req : requiredAmenities) {
            reqLower.add(req.toLowerCase().trim());
        }

        // 3. Duyệt danh sách gốc để kiểm tra
        if (originalHotelList != null) {
            for (Hotel hotel : originalHotelList) {
                List<String> hotelAmenities = hotel.getAmenities();

                // Chỉ xử lý nếu khách sạn có danh sách tiện ích
                if (hotelAmenities != null) {
                    // Tạo một danh sách tiện ích của khách sạn dạng chữ thường để so sánh
                    List<String> hotelAmenitiesLower = new ArrayList<>();
                    for (String a : hotelAmenities) {
                        hotelAmenitiesLower.add(a.toLowerCase().trim());
                    }

                    // Kiểm tra: Khách sạn phải chứa TẤT CẢ các tiện ích yêu cầu
                    // (Ví dụ: Yêu cầu Wifi + Pool -> Khách sạn phải có cả 2)
                    if (hotelAmenitiesLower.containsAll(reqLower)) {
                        filteredList.add(hotel);
                    }
                }
            }
        }

        // 4. Cập nhật kết quả lên màn hình
        hotelList.clear();
        hotelList.addAll(filteredList);
        hotelAdapter.notifyDataSetChanged();

        // Thông báo nếu không tìm thấy
        if (hotelList.isEmpty() && getContext() != null) {
            Toast.makeText(getContext(), "Không tìm thấy khách sạn phù hợp với bộ lọc", Toast.LENGTH_SHORT).show();
        }
    }
}