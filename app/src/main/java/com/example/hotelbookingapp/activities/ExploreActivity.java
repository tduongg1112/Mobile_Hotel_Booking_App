package com.example.hotelbookingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hotelbookingapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ExploreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore); // Link với file xml vừa tạo ở trên

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // Đánh dấu nút Explore đang sáng
        bottomNav.setSelectedItemId(R.id.nav_explore);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                overridePendingTransition(0, 0); // Chuyển trang ngay lập tức
                return true;
            }
            else if (id == R.id.nav_explore) {
                return true; // Đang ở đây rồi
            }
            else if (id == R.id.nav_my_booking) {
                startActivity(new Intent(getApplicationContext(), MyBookingActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            else if (id == R.id.nav_profile) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }

    // Khi quay lại (Back), đảm bảo nút Explore vẫn sáng
    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(R.id.nav_explore);
        }
    }
}