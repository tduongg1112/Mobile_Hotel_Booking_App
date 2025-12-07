package com.example.hotelbookingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelbookingapp.R;
import com.example.hotelbookingapp.models.Booking;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.util.Locale;

public class BookingActivity extends AppCompatActivity {

    private TextView tvHotelName, tvHotelAddress, tvRoomName, tvCheckIn, tvCheckOut, tvPricePerNight, tvTotalDays, tvTotalPrice;
    private Button btnConfirm;
    private ImageView btnBack;
    private ProgressBar progressBar;

    // Dữ liệu Booking
    private String hotelId, hotelName, hotelAddress, roomName, checkInDate, checkOutDate, hotelImage;
    private double pricePerNight;
    private double totalPrice;
    private int totalDays = 1; // Mặc định 1 đêm

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        initView();
        getIntentData(); // Lấy dữ liệu từ màn hình trước
        setupUI();       // Hiển thị lên màn hình

        btnBack.setOnClickListener(v -> finish());

        btnConfirm.setOnClickListener(v -> handleBooking());
    }

    private void initView() {
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

    private void getIntentData() {
        // TODO: Khi nối màn hình, hãy uncomment dòng này để lấy dữ liệu thật
        // Intent intent = getIntent();
        // hotelId = intent.getStringExtra("hotelId");
        // ...

        // DỮ LIỆU GIẢ LẬP (TEST) - Xóa khi đã có dữ liệu thật
        hotelId = "hotel_001";
        hotelName = "Khách sạn Kim Liên";
        hotelAddress = "Đào Duy Anh, Hà Nội";
        roomName = "Phòng Deluxe";
        hotelImage = "link_anh_demo"; // Cần link ảnh thật sau này
        checkInDate = "10/12/2025";
        checkOutDate = "12/12/2025";
        pricePerNight = 1500000;
        totalDays = 2;

        // Tính tổng tiền
        totalPrice = pricePerNight * totalDays;
    }

    private void setupUI() {
        tvHotelName.setText(hotelName);
        tvHotelAddress.setText(hotelAddress);
        tvRoomName.setText(roomName);
        tvCheckIn.setText(checkInDate);
        tvCheckOut.setText(checkOutDate);
        tvTotalDays.setText(totalDays + " đêm");

        // Format tiền tệ (ví dụ: 1.500.000 đ)
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tvPricePerNight.setText(formatter.format(pricePerNight));
        tvTotalPrice.setText(formatter.format(totalPrice));
    }

    private void handleBooking() {
        // 1. Kiểm tra đăng nhập
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Bạn cần đăng nhập để đặt phòng!", Toast.LENGTH_SHORT).show();
            // Chuyển sang LoginActivity tại đây nếu muốn
            return;
        }

        // 2. Hiển thị loading
        progressBar.setVisibility(View.VISIBLE);
        btnConfirm.setEnabled(false); // Chặn bấm liên tục

        // 3. Tạo object Booking
        Booking booking = new Booking(
                currentUser.getUid(),
                hotelId,
                hotelName,
                hotelAddress,
                hotelImage,
                roomName,
                checkInDate,
                checkOutDate,
                totalPrice,
                "confirmed", // Trạng thái ban đầu
                Timestamp.now()
        );

        // 4. Đẩy lên Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("bookings")
                .add(booking)
                .addOnSuccessListener(documentReference -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Đặt phòng thành công!", Toast.LENGTH_LONG).show();

                    // TODO: Chuyển sang màn hình "Lịch sử đặt phòng" (MyBookingActivity)
                    // Intent intent = new Intent(BookingActivity.this, MyBookingActivity.class);
                    // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    // startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnConfirm.setEnabled(true);
                    Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}