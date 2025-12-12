package com.example.hotelbookingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelbookingapp.activities.NotificationActivity;
import com.example.hotelbookingapp.activities.ReviewActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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