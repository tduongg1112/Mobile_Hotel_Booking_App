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
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SearchActivity extends AppCompatActivity {
    private EditText edtSearch;
    private RecyclerView rvSearchResults;
    private HomeAdapter hotelAdapter;
    private List<Hotel> allHotelsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        edtSearch = findViewById(R.id.edtSearchInput);
        rvSearchResults = findViewById(R.id.rvSearchResults);

        setupRecyclerView();
        loadAllHotels();
        setupSearchListener();
    }

    private void setupRecyclerView() {
        hotelAdapter = new HomeAdapter(this, new ArrayList<>());
        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        rvSearchResults.setAdapter(hotelAdapter);
    }

    private void loadAllHotels() {
        String collectionName = (Constants.COLLECTION_HOTELS != null) ? Constants.COLLECTION_HOTELS : "hotels";
        FirebaseFirestore.getInstance().collection(collectionName).get()
                .addOnSuccessListener(snapshots -> {
                    allHotelsList.clear();
                    for (QueryDocumentSnapshot doc : snapshots) {
                        Hotel hotel = doc.toObject(Hotel.class);
                        hotel.setId(doc.getId());
                        allHotelsList.add(hotel);
                    }
                    hotelAdapter.setHotelList(allHotelsList);
                });
    }

    private void setupSearchListener() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterHotels(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void filterHotels(String text) {
        List<Hotel> filteredList = new ArrayList<>();
        if (text == null || text.trim().isEmpty()) {
            filteredList.addAll(allHotelsList);
        } else {
            // Chuẩn hóa từ khóa: Viết thường + Bỏ dấu
            String query = removeAccent(text.toLowerCase().trim());
            for (Hotel item : allHotelsList) {
                String hotelName = (item.getName() != null) ? removeAccent(item.getName().toLowerCase()) : "";
                String hotelLoc = (item.getLocation() != null) ? removeAccent(item.getLocation().toLowerCase()) : "";

                // Logic Core: Tìm theo Tên HOẶC Địa điểm không dấu
                if (hotelName.contains(query) || hotelLoc.contains(query)) {
                    filteredList.add(item);
                }
            }
        }
        hotelAdapter.setHotelList(filteredList);
    }

    // Hàm chuẩn hóa tiếng Việt: Biến "Khách Sạn" thành "khach san"
    private String removeAccent(String s) {
        if (s == null) return "";
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replace('đ', 'd').replace('Đ', 'D');
    }
}