package com.example.hotelbookingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelbookingapp.R;
import com.example.hotelbookingapp.models.Booking;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BookingActivity extends AppCompatActivity {

    private TextView tvHotelName, tvHotelAddress, tvRoomName, tvCheckIn, tvCheckOut, tvPricePerNight, tvTotalDays, tvTotalPrice;
    private Button btnConfirm;
    private ImageView btnBack;
    private ProgressBar progressBar;

    private String hotelId, roomId, hotelName, hotelAddress, roomName;
    private double pricePerNight;
    private String checkInDate, checkOutDate;
    private long totalDays;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        initViews();
        getDataFromIntent();
        setDataToViews();

        btnBack.setOnClickListener(v -> finish());
        btnConfirm.setOnClickListener(v -> confirmBooking());
    }

    private void initViews() {
        tvHotelName = findViewById(R.id.tvHotelName);
        tvHotelAddress = findViewById(R.id.tvHotelAddress);
        tvRoomName = findViewById(R.id.tvRoomName);
        tvCheckIn = findViewById(R.id.tvCheckIn);
        tvCheckOut = findViewById(R.id.tvCheckOut);
        tvPricePerNight = findViewById(R.id.tvPricePerNight);
        tvTotalDays = findViewById(R.id.tvTotalDays);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnBack = findViewById(R.id.btnBack);
        progressBar = findViewById(R.id.progressBar);
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        hotelId = intent.getStringExtra("hotelId");
        roomId = intent.getStringExtra("roomId");
        hotelName = intent.getStringExtra("hotelName");
        hotelAddress = intent.getStringExtra("hotelAddress");
        roomName = intent.getStringExtra("roomName");
        pricePerNight = intent.getDoubleExtra("pricePerNight", 0);
        checkInDate = intent.getStringExtra("checkInDate");
        checkOutDate = intent.getStringExtra("checkOutDate");

        calculateTotalDays();
    }

    private void calculateTotalDays() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date date1 = sdf.parse(checkInDate);
            Date date2 = sdf.parse(checkOutDate);
            long diff = date2.getTime() - date1.getTime();
            totalDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
            totalDays = 0;
        }
    }

    private void setDataToViews() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        tvHotelName.setText(hotelName);
        tvHotelAddress.setText(hotelAddress);
        tvRoomName.setText(roomName);
        tvCheckIn.setText(checkInDate);
        tvCheckOut.setText(checkOutDate);
        tvPricePerNight.setText(formatter.format(pricePerNight));
        tvTotalDays.setText(totalDays + " đêm");
        tvTotalPrice.setText(formatter.format(pricePerNight * totalDays));
    }

    private void confirmBooking() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Vui lòng đăng nhập để đặt phòng", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        String bookingId = UUID.randomUUID().toString();
        String userId = currentUser.getUid();
        double totalPrice = pricePerNight * totalDays;

        Booking booking = new Booking(
                bookingId,
                hotelName,
                hotelAddress,
                checkInDate,
                checkOutDate,
                totalPrice,
                "confirmed",
                null, // hotelImage, can be added later
                userId,
                hotelId,
                roomId,
                totalDays
        );

        db.collection("bookings").document(bookingId)
                .set(booking)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(BookingActivity.this, "Đặt phòng thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(BookingActivity.this, BookingSuccessActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(BookingActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}