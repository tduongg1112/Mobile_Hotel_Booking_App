<<<<<<< HEAD
//package com.example.hotelbookingapp.activities;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.hotelbookingapp.R;
//import com.example.hotelbookingapp.models.Booking;
//import com.google.firebase.Timestamp;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.text.NumberFormat;
//import java.util.Locale;
//
//public class BookingActivity extends AppCompatActivity {
//
//    private TextView tvHotelName, tvHotelAddress, tvRoomName, tvCheckIn, tvCheckOut, tvPricePerNight, tvTotalDays, tvTotalPrice;
//    private Button btnConfirm;
//    private ImageView btnBack;
//    private ProgressBar progressBar;
//
//    // Dữ liệu Booking
//    private String hotelId, hotelName, hotelAddress, roomName, checkInDate, checkOutDate, hotelImage;
//    private double pricePerNight;
//    private double totalPrice;
//    private int totalDays = 1; // Mặc định 1 đêm
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_booking);
//
//        initView();
//        getIntentData(); // Lấy dữ liệu từ màn hình trước
//        setupUI();       // Hiển thị lên màn hình
//
//        btnBack.setOnClickListener(v -> finish());
//
//        btnConfirm.setOnClickListener(v -> handleBooking());
//    }
//
//    private void initView() {
//        tvHotelName = findViewById(R.id.tvHotelName);
//        tvHotelAddress = findViewById(R.id.tvHotelAddress);
//        tvRoomName = findViewById(R.id.tvRoomName);
//        tvCheckIn = findViewById(R.id.tvCheckIn);
//        tvCheckOut = findViewById(R.id.tvCheckOut);
//        tvPricePerNight = findViewById(R.id.tvPricePerNight);
//        tvTotalDays = findViewById(R.id.tvTotalDays);
//        tvTotalPrice = findViewById(R.id.tvTotalPrice);
//        btnConfirm = findViewById(R.id.btnConfirm);
//        btnBack = findViewById(R.id.btnBack);
//        progressBar = findViewById(R.id.progressBar);
//    }
//
//    private void getIntentData() {
//        // TODO: Khi nối màn hình, hãy uncomment dòng này để lấy dữ liệu thật
//        // Intent intent = getIntent();
//        // hotelId = intent.getStringExtra("hotelId");
//        // ...
//
//        // DỮ LIỆU GIẢ LẬP (TEST) - Xóa khi đã có dữ liệu thật
//        hotelId = "hotel_001";
//        hotelName = "Khách sạn Kim Liên";
//        hotelAddress = "Đào Duy Anh, Hà Nội";
//        roomName = "Phòng Deluxe";
//        hotelImage = "link_anh_demo"; // Cần link ảnh thật sau này
//        checkInDate = "10/12/2025";
//        checkOutDate = "12/12/2025";
//        pricePerNight = 1500000;
//        totalDays = 2;
//
//        // Tính tổng tiền
//        totalPrice = pricePerNight * totalDays;
//    }
//
//    private void setupUI() {
//        tvHotelName.setText(hotelName);
//        tvHotelAddress.setText(hotelAddress);
//        tvRoomName.setText(roomName);
//        tvCheckIn.setText(checkInDate);
//        tvCheckOut.setText(checkOutDate);
//        tvTotalDays.setText(totalDays + " đêm");
//
//        // Format tiền tệ (ví dụ: 1.500.000 đ)
//        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
//        tvPricePerNight.setText(formatter.format(pricePerNight));
//        tvTotalPrice.setText(formatter.format(totalPrice));
//    }
//
//    private void handleBooking() {
//        // 1. Kiểm tra đăng nhập
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (currentUser == null) {
//            Toast.makeText(this, "Bạn cần đăng nhập để đặt phòng!", Toast.LENGTH_SHORT).show();
//            // Chuyển sang LoginActivity tại đây nếu muốn
//            return;
//        }
//
//        // 2. Hiển thị loading
//        progressBar.setVisibility(View.VISIBLE);
//        btnConfirm.setEnabled(false); // Chặn bấm liên tục
//
//        // 3. Tạo object Booking
//        Booking booking = new Booking(
//                currentUser.getUid(),
//                hotelId,
//                hotelName,
//                hotelAddress,
//                hotelImage,
//                roomName,
//                checkInDate,
//                checkOutDate,
//                totalPrice,
//                "confirmed", // Trạng thái ban đầu
//                Timestamp.now()
//        );
//
//        // 4. Đẩy lên Firestore
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("bookings")
//                .add(booking)
//                .addOnSuccessListener(documentReference -> {
//                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(this, "Đặt phòng thành công!", Toast.LENGTH_LONG).show();
//
//                    // TODO: Chuyển sang màn hình "Lịch sử đặt phòng" (MyBookingActivity)
//                    // Intent intent = new Intent(BookingActivity.this, MyBookingActivity.class);
//                    // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    // startActivity(intent);
//                    finish();
//                })
//                .addOnFailureListener(e -> {
//                    progressBar.setVisibility(View.GONE);
//                    btnConfirm.setEnabled(true);
//                    Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                });
//    }
//}
=======
package com.example.hotelbookingapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelbookingapp.R;
import com.example.hotelbookingapp.models.Booking;
import com.example.hotelbookingapp.models.Hotel;
import com.example.hotelbookingapp.models.Room;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BookingActivity extends AppCompatActivity {

    // Views
    private TextView tvHotelName, tvHotelAddress, tvRoomName;
    private TextView tvCheckIn, tvCheckOut;
    private TextView tvPricePerNight, tvTotalDays, tvTotalPrice;
    private CalendarView calendarView;
    private Button btnConfirm;
    private ImageView btnBack;
    private ProgressBar progressBar;

    // Dữ liệu
    private Hotel currentHotel;
    private Room currentRoom;
    private Calendar checkInCalendar;
    private int totalDays = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date); // Đã khớp file XML

        // Nhận dữ liệu
        if (getIntent().hasExtra("hotel_item") && getIntent().hasExtra("room_item")) {
            currentHotel = getIntent().getParcelableExtra("hotel_item");
            currentRoom = (Room) getIntent().getSerializableExtra("room_item");
        }

        if (currentHotel == null || currentRoom == null) {
            Toast.makeText(this, "Lỗi dữ liệu đặt phòng!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupDefaultDate();
        setupUI();
        setupEvents();
    }

    private void initViews() {
        // Ánh xạ đúng ID từ file XML mới
        tvHotelName = findViewById(R.id.tvHotelName);
        tvHotelAddress = findViewById(R.id.tvHotelAddress);
        tvRoomName = findViewById(R.id.tvRoomName);

        tvCheckIn = findViewById(R.id.tvCheckIn);
        tvCheckOut = findViewById(R.id.tvCheckOut);

        tvPricePerNight = findViewById(R.id.tvPricePerNight);
        tvTotalDays = findViewById(R.id.tvTotalDays);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);

        calendarView = findViewById(R.id.calendarView);
        btnConfirm = findViewById(R.id.btnConfirm); // Đúng ID btnConfirm
        btnBack = findViewById(R.id.btnBack);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupDefaultDate() {
        checkInCalendar = Calendar.getInstance();
        if (calendarView != null) {
            calendarView.setMinDate(System.currentTimeMillis() - 1000);
        }
    }

    private void setupUI() {
        tvHotelName.setText(currentHotel.getName());
        tvHotelAddress.setText(currentHotel.getLocation());
        tvRoomName.setText(currentRoom.getName());
        updateDateAndPriceDisplay();
    }

    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());

        if (calendarView != null) {
            calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
                checkInCalendar.set(year, month, dayOfMonth);
                updateDateAndPriceDisplay();
            });
        }

        btnConfirm.setOnClickListener(v -> handleBooking());
    }

    private void updateDateAndPriceDisplay() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("vi", "VN"));

        // Ngày Check-in
        String strCheckIn = sdf.format(checkInCalendar.getTime());
        tvCheckIn.setText(strCheckIn);

        // Ngày Check-out (+1 ngày)
        Calendar checkOutCal = (Calendar) checkInCalendar.clone();
        checkOutCal.add(Calendar.DAY_OF_MONTH, totalDays);
        String strCheckOut = sdf.format(checkOutCal.getTime());
        tvCheckOut.setText(strCheckOut);

        // Tính tiền
        double price = currentRoom.getPrice();
        double total = price * totalDays;

        NumberFormat currency = NumberFormat.getInstance(new Locale("vi", "VN"));
        tvPricePerNight.setText(currency.format(price));
        tvTotalDays.setText(totalDays + " đêm");
        tvTotalPrice.setText(currency.format(total));
    }

    private void handleBooking() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Vui lòng đăng nhập!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
        btnConfirm.setEnabled(false);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("vi", "VN"));
        String strCheckIn = sdf.format(checkInCalendar.getTime());

        Calendar checkOutCal = (Calendar) checkInCalendar.clone();
        checkOutCal.add(Calendar.DAY_OF_MONTH, totalDays);
        String strCheckOut = sdf.format(checkOutCal.getTime());

        String img = "";
        if (currentHotel.getImageUrl() != null) img = currentHotel.getImageUrl();
        else if (currentHotel.getImages() != null && !currentHotel.getImages().isEmpty()) img = currentHotel.getImages().get(0);

        Booking booking = new Booking(
                user.getUid(),
                currentHotel.getId(),
                currentHotel.getName(),
                currentHotel.getLocation(),
                img,
                currentRoom.getName(),
                strCheckIn,
                strCheckOut,
                currentRoom.getPrice() * totalDays,
                "CONFIRMED",
                Timestamp.now()
        );

        FirebaseFirestore.getInstance().collection("bookings")
                .add(booking)
                .addOnSuccessListener(doc -> {
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Đặt phòng thành công!", Toast.LENGTH_LONG).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                    btnConfirm.setEnabled(true);
                    Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
>>>>>>> 45172f9a3310fc6720bdf3d0e0e59d8d1d28e484
