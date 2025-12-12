package com.example.hotelbookingapp.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hotelbookingapp.R;
import com.example.hotelbookingapp.adapters.HomeAdapter;
import com.example.hotelbookingapp.models.Hotel;
import com.example.hotelbookingapp.utils.Constants;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText edtSearch;
    private RecyclerView rvSearchResults;
    private HomeAdapter hotelAdapter;
    private FirebaseFirestore db;
    private List<Hotel> allHotelsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Đảm bảo bạn đã có file activity_search.xml (xem mục số 4 bên dưới)
        setContentView(R.layout.activity_search);

        db = FirebaseFirestore.getInstance();

        // Ánh xạ Views (ID phải khớp với activity_search.xml)
        edtSearch = findViewById(R.id.edtSearchInput);
        rvSearchResults = findViewById(R.id.rvSearchResults);

        setupRecyclerView();
        loadAllHotels(); // Tải dữ liệu về trước
        setupSearchListener();
    }

    private void setupRecyclerView() {
        hotelAdapter = new HomeAdapter(this, new ArrayList<>());
        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        rvSearchResults.setAdapter(hotelAdapter);
    }

    private void loadAllHotels() {
        // Dùng Constants.COLLECTION_HOTELS nếu có, hoặc dùng chuỗi cứng "hotels"
        String collectionName = (Constants.COLLECTION_HOTELS != null) ? Constants.COLLECTION_HOTELS : "hotels";

        db.collection(collectionName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    allHotelsList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Hotel hotel = doc.toObject(Hotel.class);
                        hotel.setId(doc.getId());
                        allHotelsList.add(hotel);
                    }
                    // Ban đầu hiển thị toàn bộ danh sách
                    hotelAdapter.setHotelList(allHotelsList);
                });
    }

    private void setupSearchListener() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterHotels(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterHotels(String text) {
        List<Hotel> filteredList = new ArrayList<>();
        // Nếu ô tìm kiếm rỗng, hiển thị lại toàn bộ
        if (text == null || text.isEmpty()) {
            filteredList.addAll(allHotelsList);
        } else {
            String searchText = text.toLowerCase().trim();
            for (Hotel item : allHotelsList) {
                // Logic tìm kiếm: Tìm theo tên HOẶC địa điểm
                if ((item.getName() != null && item.getName().toLowerCase().contains(searchText)) ||
                        (item.getLocation() != null && item.getLocation().toLowerCase().contains(searchText))) {
                    filteredList.add(item);
                }
            }
        }
        // Gọi hàm update trong Adapter
        hotelAdapter.setHotelList(filteredList);
    }
}