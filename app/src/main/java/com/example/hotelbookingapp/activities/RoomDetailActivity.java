package com.example.hotelbookingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.hotelbookingapp.R;
import com.example.hotelbookingapp.models.Hotel;
import com.example.hotelbookingapp.models.Room;
import com.example.hotelbookingapp.utils.GlideUtils;
import java.text.NumberFormat;
import java.util.Locale;

public class RoomDetailActivity extends AppCompatActivity {

    private ImageView imgHeader, btnBack;
    private TextView tvName, tvCapacity, tvFeatures, tvDesc, tvPrice;
    private Button btnBooking;

    private Room currentRoom;
    private Hotel currentHotel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);

        // Nhận dữ liệu
        if (getIntent().hasExtra("room_item") && getIntent().hasExtra("hotel_item")) {
            currentRoom = (Room) getIntent().getSerializableExtra("room_item");
            currentHotel = getIntent().getParcelableExtra("hotel_item");
        }

        if (currentRoom == null) {
            finish();
            return;
        }

        initViews();
        setupData();
        setupEvents();
    }

    private void initViews() {
        imgHeader = findViewById(R.id.imgRoomHeader);
        btnBack = findViewById(R.id.btnBack);
        tvName = findViewById(R.id.tvRoomName);
        tvCapacity = findViewById(R.id.tvRoomCapacity);
        tvFeatures = findViewById(R.id.tvRoomFeatures);
        tvDesc = findViewById(R.id.tvRoomDesc);
        tvPrice = findViewById(R.id.tvRoomPrice);
        btnBooking = findViewById(R.id.btnBooking);
    }

    private void setupData() {
        tvName.setText(currentRoom.getName());
        tvCapacity.setText("Sức chứa: " + currentRoom.getMaxGuests() + " người");

        // Hiển thị giá
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        tvPrice.setText(formatter.format(currentRoom.getPrice()) + " đ");

        // Hiển thị mô tả (nếu có trường description trong Room thì dùng, ko thì dùng tạm text)
        // tvDesc.setText(currentRoom.getDescription());
        tvDesc.setText("Trải nghiệm không gian nghỉ dưỡng đẳng cấp với đầy đủ tiện nghi, view đẹp thoáng mát...");

        // Hiển thị tiện ích
        if (currentRoom.getFeatures() != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                tvFeatures.setText(String.join(" • ", currentRoom.getFeatures()));
            } else {
                tvFeatures.setText(currentRoom.getFeatures().toString());
            }
        }

        // --- HIỂN THỊ ẢNH (LOGIC HYBRID) ---
        // 1. Tìm ảnh local: hotel_022_dlx
        String localImgName = currentHotel.getId() + "_" + currentRoom.getId();
        int resId = getResources().getIdentifier(localImgName, "drawable", getPackageName());

        if (resId != 0) {
            imgHeader.setImageResource(resId);
        } else {
            // 2. Tải ảnh mạng
            if (currentRoom.getImage() != null && !currentRoom.getImage().isEmpty()) {
                GlideUtils.loadHotelImage(this, currentRoom.getImage(), imgHeader);
            } else {
                imgHeader.setImageResource(R.drawable.placeholder_image);
            }
        }
    }

    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());

        // KHI BẤM "ĐẶT NGAY" -> MỚI SANG BOOKING ACTIVITY (CHỌN NGÀY)
        btnBooking.setOnClickListener(v -> {
            Intent intent = new Intent(this, BookingActivity.class);
            intent.putExtra("room_item", currentRoom);
            intent.putExtra("hotel_item", currentHotel);
            startActivity(intent);
        });
    }
}