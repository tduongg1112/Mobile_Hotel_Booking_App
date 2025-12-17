package com.example.hotelbookingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView; // Sửa lại import cho đúng loại nút Back
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelbookingapp.R;
import com.example.hotelbookingapp.adapters.BookingAdapter;
import com.example.hotelbookingapp.models.Booking;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MyBookingActivity extends AppCompatActivity implements BookingAdapter.IBookingHandler {

    private RecyclerView rvBookings;
    private BookingAdapter bookingAdapter;
    private List<Booking> mListBooking;
    private FirebaseFirestore db;
    private ImageView btnBack; // Trong XML là ImageView
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_booking);

        db = FirebaseFirestore.getInstance();

        initViews();
        setupBottomNavigation();
        setupRecyclerView();
    }

    private void initViews() {
        // --- SỬA LỖI Ở ĐÂY: Ánh xạ đúng ID trong file XML ---
        rvBookings = findViewById(R.id.rcv_bookings);      // XML id: rcv_bookings
        tvEmpty = findViewById(R.id.tv_empty_booking);     // XML id: tv_empty_booking
        btnBack = findViewById(R.id.btnBack);              // XML id: btnBack

        // Bắt sự kiện nút Back
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                startActivity(new Intent(MyBookingActivity.this, HomeActivity.class));
                finish();
            });
        }
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav == null) return;

        bottomNav.setSelectedItemId(R.id.nav_my_booking);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(MyBookingActivity.this, HomeActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            else if (id == R.id.nav_explore) {
                startActivity(new Intent(MyBookingActivity.this, ExploreActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            else if (id == R.id.nav_my_booking) {
                return true;
            }
            else if (id == R.id.nav_profile) {
                startActivity(new Intent(MyBookingActivity.this, ProfileActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBookingHistory();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav != null) bottomNav.setSelectedItemId(R.id.nav_my_booking);
    }

    private void setupRecyclerView() {
        mListBooking = new ArrayList<>();
        // Nếu Adapter báo lỗi constructor, hãy kiểm tra lại file BookingAdapter
        bookingAdapter = new BookingAdapter(this, mListBooking, this);
        rvBookings.setLayoutManager(new LinearLayoutManager(this));
        rvBookings.setAdapter(bookingAdapter);
    }

    private void loadBookingHistory() {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId == null) return;

        db.collection("bookings")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(snapshots -> {
                    mListBooking.clear();
                    for (DocumentSnapshot doc : snapshots) {
                        Booking booking = doc.toObject(Booking.class);
                        if (booking != null) {
                            booking.setId(doc.getId());
                            mListBooking.add(booking);
                        }
                    }
                    bookingAdapter.notifyDataSetChanged();

                    // Ẩn hiện thông báo trống
                    if (tvEmpty != null) {
                        tvEmpty.setVisibility(mListBooking.isEmpty() ? View.VISIBLE : View.GONE);
                        rvBookings.setVisibility(mListBooking.isEmpty() ? View.GONE : View.VISIBLE);
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onCancelClick(Booking booking) {
        // Chuyển sang màn hình hủy phòng hoặc xử lý hủy trực tiếp tại đây
        // Đảm bảo bạn đã có CancelBookingActivity nếu dùng Intent
        Intent intent = new Intent(MyBookingActivity.this, CancelBookingActivity.class);
        intent.putExtra("BOOKING_ID", booking.getId());
        intent.putExtra("HOTEL_NAME", booking.getHotelName());
        startActivity(intent);
    }
}