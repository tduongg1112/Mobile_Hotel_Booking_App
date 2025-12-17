package com.example.hotelbookingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelbookingapp.activities.NotificationActivity; // Đảm bảo đã có file này
import com.example.hotelbookingapp.activities.ReviewActivity;       // Đảm bảo đã có file này
// import các Activity trang con của bạn (Explore, Booking, Profile)
// ví dụ: import com.example.hotelbookingapp.activities.ExploreActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // --- PHẦN 1: LOGIC CỦA NHÓM (GIỮ NGUYÊN) ---
        Button btnReview = findViewById(R.id.btn_open_review);
        Button btnNoti = findViewById(R.id.btn_open_noti);

        btnReview.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReviewActivity.class);
            intent.putExtra("HOTEL_ID", "hotel_001");
            startActivity(intent);
        });

        btnNoti.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
            startActivity(intent);
        });

        // --- PHẦN 2: LOGIC MENU CỦA BẠN (THÊM VÀO) ---
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // Đánh dấu nút Home là đang chọn
        bottomNav.setSelectedItemId(R.id.nav_home);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                // Đang ở Home rồi thì không làm gì
                return true;
            }
            else if (itemId == R.id.nav_explore) {
                // Chuyển sang trang Explore
                // Intent intent = new Intent(MainActivity.this, ExploreActivity.class);
                // startActivity(intent);
                // overridePendingTransition(0, 0); // Tắt hiệu ứng trượt
                return true;
            }
            else if (itemId == R.id.nav_my_booking) {
                // Chuyển sang trang My Booking
                // Intent intent = new Intent(MainActivity.this, MyBookingActivity.class);
                // startActivity(intent);
                // overridePendingTransition(0, 0);
                return true;
            }
            else if (itemId == R.id.nav_profile) {
                // Chuyển sang trang Profile
                // Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                // startActivity(intent);
                // overridePendingTransition(0, 0);
                return true;
            }
            return false;

        Button btnReview = findViewById(R.id.btn_open_review);
        Button btnNoti = findViewById(R.id.btn_open_noti);

        // 1. Mở màn hình Review (Có truyền ID khách sạn mẫu)
        btnReview.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReviewActivity.class);
            intent.putExtra("HOTEL_ID", "hotel_001"); // Giả lập đang xem khách sạn 001
            startActivity(intent);
        });

        // 2. Mở màn hình Thông báo
        btnNoti.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
            startActivity(intent);
        });
    }
}