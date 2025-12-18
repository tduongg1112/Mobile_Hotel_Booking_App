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
import androidx.cardview.widget.CardView;
import androidx.core.util.Pair;

import com.example.hotelbookingapp.R;
import com.example.hotelbookingapp.activities.BookingSuccessActivity;
import com.example.hotelbookingapp.models.Booking;
import com.example.hotelbookingapp.models.Hotel;
import com.example.hotelbookingapp.models.NotificationItem;
import com.example.hotelbookingapp.models.Room;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import android.util.Log;

public class BookingActivity extends AppCompatActivity {

    private TextView tvHotelName, tvHotelAddress, tvRoomName;
    private TextView tvCheckIn, tvCheckOut;
    private TextView tvPricePerNight, tvTotalDays, tvTotalPrice, tvBottomTotalPrice;
    private CardView dateSelectionCard;
    private Button btnConfirm;
    private ImageView btnBack;
    private ProgressBar progressBar;

    private Hotel currentHotel;
    private Room currentRoom;
    private LocalDate checkInDate = null;
    private LocalDate checkOutDate = null;
    private long totalDays = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date);

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
        setupUI();
        setupEvents();
        updateDateAndPriceDisplay();
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
        tvBottomTotalPrice = findViewById(R.id.tvPrice);
        dateSelectionCard = findViewById(R.id.dateSelectionCard);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnBack = findViewById(R.id.btnBack);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupUI() {
        tvHotelName.setText(currentHotel.getName());
        tvHotelAddress.setText(currentHotel.getLocation());
        tvRoomName.setText(currentRoom.getName());
        NumberFormat currency = NumberFormat.getInstance(new Locale("vi", "VN"));
        tvPricePerNight.setText(currency.format(currentRoom.getPrice()));
    }

    private void showDatePicker() {
        // Tạo CalendarConstraints để không cho chọn ngày quá khứ
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setValidator(DateValidatorPointForward.now());

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Chọn ngày nhận và trả phòng");
        builder.setCalendarConstraints(constraintsBuilder.build());

        // Nếu đã chọn ngày trước đó, hiển thị lại trên picker
        if (checkInDate != null && checkOutDate != null) {
            long start = checkInDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
            long end = checkOutDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
            builder.setSelection(new Pair<>(start, end));
        }

        final MaterialDatePicker<Pair<Long, Long>> picker = builder.build();
        picker.show(getSupportFragmentManager(), picker.toString());

        picker.addOnPositiveButtonClickListener(selection -> {
            if (selection.first != null && selection.second != null) {
                checkInDate = Instant.ofEpochMilli(selection.first).atZone(ZoneId.systemDefault()).toLocalDate();
                checkOutDate = Instant.ofEpochMilli(selection.second).atZone(ZoneId.systemDefault()).toLocalDate();
                updateDateAndPriceDisplay();
            }
        });
    }

    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());
        btnConfirm.setOnClickListener(v -> handleBooking());
        dateSelectionCard.setOnClickListener(v -> showDatePicker());
    }

    private void updateDateAndPriceDisplay() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        if (checkInDate != null) {
            tvCheckIn.setText(sdf.format(Date.from(checkInDate.atStartOfDay(ZoneId.systemDefault()).toInstant())));
        } else {
            tvCheckIn.setText("-- / -- / ----");
        }

        if (checkOutDate != null) {
            tvCheckOut.setText(sdf.format(Date.from(checkOutDate.atStartOfDay(ZoneId.systemDefault()).toInstant())));
            totalDays = java.time.temporal.ChronoUnit.DAYS.between(checkInDate, checkOutDate);
            if (totalDays < 0) totalDays = 0; // Đảm bảo số ngày không âm
        } else {
            tvCheckOut.setText("-- / -- / ----");
            totalDays = 0;
        }

        double price = currentRoom.getPrice();
        double total = price * totalDays;

        NumberFormat currency = NumberFormat.getInstance(new Locale("vi", "VN"));
        tvTotalDays.setText(totalDays + " đêm");
        tvTotalPrice.setText(currency.format(total));
        if (tvBottomTotalPrice != null) {
            tvBottomTotalPrice.setText(currency.format(total));
        }

        // Kích hoạt nút xác nhận chỉ khi đã chọn đủ ngày
        btnConfirm.setEnabled(checkInDate != null && checkOutDate != null && totalDays > 0);
    }

    private void handleBooking() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Vui lòng đăng nhập!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (checkInDate == null || checkOutDate == null || totalDays <= 0) {
            Toast.makeText(this, "Vui lòng chọn ngày nhận và trả phòng hợp lệ.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
        btnConfirm.setEnabled(false);

        checkRoomAvailability();
    }

    private void checkRoomAvailability() {
        FirebaseFirestore.getInstance().collection("bookings")
                .whereEqualTo("hotelId", currentHotel.getId())
                .whereEqualTo("roomName", currentRoom.getName())
                .whereEqualTo("status", "CONFIRMED")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int bookedCount = 0;
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Booking existingBooking = doc.toObject(Booking.class);
                        try {
                            LocalDate existingCheckIn = LocalDate.parse(existingBooking.getCheckInDate(), formatter);
                            LocalDate existingCheckOut = LocalDate.parse(existingBooking.getCheckOutDate(), formatter);

                            // Check for overlap: (StartA < EndB) and (EndA > StartB)
                            if (checkInDate.isBefore(existingCheckOut) && checkOutDate.isAfter(existingCheckIn)) {
                                bookedCount++;
                            }
                        } catch (Exception e) {
                            Log.e("BookingActivity", "Error parsing date from booking: " + doc.getId(), e);
                        }
                    }

                    if (bookedCount >= currentRoom.getQuantity()) {
                        // Not available
                        Toast.makeText(this, "Hết phòng trong khoảng ngày này. Vui lòng chọn lại.", Toast.LENGTH_LONG).show();
                        if (progressBar != null) progressBar.setVisibility(View.GONE);
                        btnConfirm.setEnabled(true);
                    } else {
                        // Available, proceed to book
                        proceedWithBooking();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(this, "Lỗi khi kiểm tra phòng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                    btnConfirm.setEnabled(true);
                });
    }

    private void proceedWithBooking() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return; // Should not happen due to earlier check

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String strCheckIn = sdf.format(Date.from(checkInDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        String strCheckOut = sdf.format(Date.from(checkOutDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

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

                    // Create notification
                    String userId = user.getUid();
                    String title = "Đặt phòng thành công!";
                    String body = "Bạn đã đặt thành công phòng " + currentRoom.getName() + " tại khách sạn " + currentHotel.getName() + ".";
                    NotificationItem notification = new NotificationItem(userId, title, body, new java.util.Date());

                    FirebaseFirestore.getInstance().collection("notifications")
                            .add(notification)
                            .addOnSuccessListener(notificationDoc -> Log.d("BookingActivity", "Notification created for successful booking."))
                            .addOnFailureListener(e -> Log.e("BookingActivity", "Error creating notification", e));

                    // Chuyển sang màn hình thành công
                    Intent intent = new Intent(BookingActivity.this, BookingSuccessActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                    btnConfirm.setEnabled(true);
                    Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}