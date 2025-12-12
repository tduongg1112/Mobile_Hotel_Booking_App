package com.example.hotelbookingapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelbookingapp.R;

public class CancelBookingActivity extends AppCompatActivity {

    private ImageView btnBack;
    private Button btnConfirmCancel;
    private RadioGroup radioGroupReason;
    private TextView tvHotelName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_booking);

        initView();

        // Nhận tên khách sạn từ màn hình MyBooking (nếu có truyền qua)
        // String hotelName = getIntent().getStringExtra("HOTEL_NAME");
        // if (hotelName != null) tvHotelName.setText(hotelName);

        btnBack.setOnClickListener(v -> finish());

        btnConfirmCancel.setOnClickListener(v -> {
            // Kiểm tra xem đã chọn lý do chưa
            if (radioGroupReason.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Vui lòng chọn lý do hủy phòng", Toast.LENGTH_SHORT).show();
            } else {
                // Xử lý hủy thành công
                Toast.makeText(this, "Yêu cầu hủy phòng đã được gửi thành công!", Toast.LENGTH_LONG).show();
                finish(); // Quay lại màn hình trước
            }
        });
    }

    private void initView() {
        btnBack = findViewById(R.id.btnBack);
        btnConfirmCancel = findViewById(R.id.btnConfirmCancel);
        radioGroupReason = findViewById(R.id.radioGroupReason);
        tvHotelName = findViewById(R.id.tvHotelName);
    }
}

//**Cách nối màn hình:**
//Trong Adapter của `MyBookingActivity` (nơi bạn xử lý danh sách các phòng), khi người dùng bấm vào nút "Hủy" màu đỏ mà chúng ta vừa thêm, bạn hãy gọi lệnh chuyển màn hình:
//
//        ```java
//Intent intent = new Intent(context, CancelBookingActivity.class);
//// intent.putExtra("HOTEL_NAME", "Khách sạn Kim Liên"); // Truyền dữ liệu nếu cần
//context.startActivity(intent);