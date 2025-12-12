package com.example.hotelbookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
<<<<<<< HEAD
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// QUAN TRỌNG: Import R của dự án bạn (KHÔNG ĐƯỢC CÓ import android.R;)
import com.example.hotelbookingapp.R;

import com.example.hotelbookingapp.adapters.HomeAdapter;
import com.example.hotelbookingapp.models.Hotel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    // Khai báo Views
    private EditText etLocation;
    private Button btnSearch;
    private RecyclerView rvHotels;

    // Adapter và List dữ liệu
    private HomeAdapter hotelAdapter;
    private List<Hotel> hotelList;

=======
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import com.example.hotelbookingapp.R;
// Import đầy đủ 4 fragment
import com.example.hotelbookingapp.fragments.BookingFragment;
import com.example.hotelbookingapp.fragments.ExploreFragment;
import com.example.hotelbookingapp.fragments.HomeFragment;
import com.example.hotelbookingapp.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

>>>>>>> 45172f9a3310fc6720bdf3d0e0e59d8d1d28e484
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

<<<<<<< HEAD
        // 1. Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        // 2. Ánh xạ Views
        initViews();

        // 3. Setup RecyclerView
        setupRecyclerView();

        // 4. Tải dữ liệu mặc định
        loadDefaultHotels();

        // 5. Xử lý sự kiện nút Tìm kiếm
        btnSearch.setOnClickListener(v -> performSearch());
    }

    private void initViews() {
        // Đảm bảo ID này khớp với activity_home.xml
        etLocation = findViewById(R.id.etLocation);
        btnSearch = findViewById(R.id.btnSearch);
        rvHotels = findViewById(R.id.rvHotels);
    }

    private void setupRecyclerView() {
        hotelList = new ArrayList<>();
        hotelAdapter = new HomeAdapter(this, hotelList);
        rvHotels.setLayoutManager(new LinearLayoutManager(this));
        rvHotels.setAdapter(hotelAdapter);
    }

    private void loadDefaultHotels() {
        queryFilteredHotels("");
    }

    private void performSearch() {
        String keyword = etLocation.getText().toString().trim();
        queryFilteredHotels(keyword);
    }

    private void queryFilteredHotels(String keyword) {
        // Dùng chuỗi cứng "hotels" để tránh lỗi nếu thiếu file Constants
        Query query = db.collection("hotels");

        if (!keyword.isEmpty()) {
            // Tìm kiếm theo mảng từ khóa (searchKeywords)
            query = query.whereArrayContains("searchKeywords", keyword.toLowerCase());
        } else {
            // Nếu không tìm gì thì sắp xếp theo tên
            query = query.orderBy("name");
        }

        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    hotelList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Hotel hotel = document.toObject(Hotel.class);
                        hotel.setId(document.getId());
                        hotelList.add(hotel);
                    }
                    hotelAdapter.notifyDataSetChanged();

                    if (hotelList.isEmpty()) {
                        Toast.makeText(HomeActivity.this, "Không tìm thấy khách sạn nào!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("HomeActivity", "Lỗi lấy dữ liệu: ", e);
                    Toast.makeText(HomeActivity.this, "Lỗi kết nối: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
=======
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // Mặc định load Home
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (id == R.id.nav_explore) {
                selectedFragment = new ExploreFragment();
            } else if (id == R.id.nav_booking) {
                selectedFragment = new BookingFragment();
            } else if (id == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                return true;
            }
            return false;
        });
>>>>>>> 45172f9a3310fc6720bdf3d0e0e59d8d1d28e484
    }
}