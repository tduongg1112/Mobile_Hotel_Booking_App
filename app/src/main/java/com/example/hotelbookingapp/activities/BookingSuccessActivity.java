package com.example.hotelbookingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelbookingapp.R;

public class BookingSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_success);

        Button btnHome = findViewById(R.id.btnHome);

        btnHome.setOnClickListener(v -> {
            // Quay về màn hình Home và xóa stack (không cho back lại màn hình đặt phòng)
            Intent intent = new Intent(BookingSuccessActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}