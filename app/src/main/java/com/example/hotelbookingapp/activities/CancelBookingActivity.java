package com.example.hotelbookingapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.hotelbookingapp.R;
import com.example.hotelbookingapp.models.Booking;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CancelBookingActivity extends AppCompatActivity {

    private ImageView btnBack, imgHotel;
    private Button btnConfirmCancel;
    private RadioGroup radioGroupReason;
    private TextView tvHotelName, tvBookingDate, tvBookingPrice;
    private FirebaseFirestore db;
    private String bookingId;
    private Booking currentBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_booking);

        db = FirebaseFirestore.getInstance();

        initView();

        // Nhận toàn bộ object Booking từ Intent
        if (getIntent().hasExtra("booking_object")) {
            currentBooking = (Booking) getIntent().getSerializableExtra("booking_object");
        }

        if (currentBooking == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin đặt phòng.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Gán bookingId để dùng cho việc hủy
        bookingId = currentBooking.getId();
        populateBookingDetails();


        btnBack.setOnClickListener(v -> finish());

        btnConfirmCancel.setOnClickListener(v -> {
            if (radioGroupReason.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Vui lòng chọn lý do hủy phòng", Toast.LENGTH_SHORT).show();
            } else {
                cancelBookingInFirestore();
            }
        });
    }

    private void initView() {
        btnBack = findViewById(R.id.btnBack);
        btnConfirmCancel = findViewById(R.id.btnConfirmCancel);
        radioGroupReason = findViewById(R.id.radioGroupReason);
        tvHotelName = findViewById(R.id.tvHotelName);
        imgHotel = findViewById(R.id.imgHotel);
        tvBookingDate = findViewById(R.id.tvBookingDate);
        tvBookingPrice = findViewById(R.id.tvBookingPrice);
    }

    private void populateBookingDetails() {
        tvHotelName.setText(currentBooking.getHotelName());

        // Ghép chuỗi ngày và hiển thị
        String dateText = currentBooking.getCheckInDate() + " - " + currentBooking.getCheckOutDate();
        tvBookingDate.setText(dateText);

        // Format và hiển thị giá
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tvBookingPrice.setText(currencyFormatter.format(currentBooking.getTotalPrice()));

        // --- LOGIC HIỂN THỊ ẢNH HYBRID (LOCAL -> NETWORK) ---
        String imageUrl = currentBooking.getHotelImage();
        String hotelId = currentBooking.getHotelId();

        // 1. Ưu tiên tải URL từ chính object booking
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(imgHotel);
            return;
        }

        // 2. Nếu không có, thử tìm ảnh trong resource drawable bằng hotelId
        if (hotelId != null && !hotelId.isEmpty()) {
            int resId = getResources().getIdentifier(hotelId, "drawable", getPackageName());
            if (resId != 0) {
                imgHotel.setImageResource(resId);
                return;
            }
        }

        // 3. Nếu vẫn không có, tải từ Firestore như một phương án cuối
        if (hotelId != null && !hotelId.isEmpty()) {
            db.collection("hotels").document(hotelId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String urlToLoad = documentSnapshot.getString("imageUrl");
                            if (urlToLoad == null || urlToLoad.isEmpty()) {
                                List<String> images = (List<String>) documentSnapshot.get("images");
                                if (images != null && !images.isEmpty()) {
                                    urlToLoad = images.get(0);
                                }
                            }

                            Glide.with(CancelBookingActivity.this)
                                    .load(urlToLoad)
                                    .placeholder(R.drawable.placeholder_image)
                                    .error(R.drawable.error_image)
                                    .into(imgHotel);
                        } else {
                            imgHotel.setImageResource(R.drawable.error_image);
                        }
                    })
                    .addOnFailureListener(e -> {
                        imgHotel.setImageResource(R.drawable.error_image);
                    });
        } else {
            // 4. Fallback cuối cùng nếu không có gì cả
            imgHotel.setImageResource(R.drawable.placeholder_image);
        }
    }

    private void cancelBookingInFirestore() {
        if (bookingId == null || bookingId.isEmpty()) {
            Toast.makeText(this, "Lỗi: Mã đặt phòng không hợp lệ.", Toast.LENGTH_SHORT).show();
            return;
        }
        btnConfirmCancel.setEnabled(false); // Vô hiệu hóa nút để tránh nhấn nhiều lần
        db.collection("bookings").document(bookingId)
                .update("status", "CANCELLED")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Yêu cầu hủy phòng đã được gửi thành công!", Toast.LENGTH_LONG).show();
                    finish(); // Quay lại màn hình trước, onResume của MyBookingActivity sẽ được gọi
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi hủy phòng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    btnConfirmCancel.setEnabled(true); // Kích hoạt lại nút nếu có lỗi
                });
    }
}