package com.example.hotelbookingapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelbookingapp.R;
import com.example.hotelbookingapp.adapters.BookingAdapter; // Bạn cần tạo file này (xem hướng dẫn bên dưới nếu chưa có)
import com.example.hotelbookingapp.models.Booking;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyBookingActivity extends AppCompatActivity {

    private RecyclerView rcvBookings;
    private BookingAdapter bookingAdapter;
    private List<Booking> bookingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_booking); // Đảm bảo bạn đã tạo layout này

        initView();
        loadUserBookings();
    }

    private void initView() {
        rcvBookings = findViewById(R.id.rcv_bookings); // ID trong file XML của bạn

        bookingList = new ArrayList<>();

        // Khởi tạo Adapter
        // Lưu ý: Nếu BookingAdapter của bạn chưa có constructor này, bạn cần chỉnh lại cho khớp
        // Ở đây tôi truyền 'this' để làm listener cho sự kiện Hủy phòng
        bookingAdapter = new BookingAdapter(bookingList, this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvBookings.setLayoutManager(linearLayoutManager);
        rcvBookings.setAdapter(bookingAdapter);
    }

    // --- Logic Load danh sách ---
    private void loadUserBookings() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Query: Lấy bookings của user hiện tại, sắp xếp theo ngày đặt mới nhất
        db.collection("bookings")
                .whereEqualTo("userId", userId)
                .orderBy("bookingDate", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("Firestore", "Listen failed.", error);
                        return;
                    }

                    if (value != null) {
                        // Xóa danh sách cũ để cập nhật mới
                        bookingList.clear();

                        for (QueryDocumentSnapshot doc : value) {
                            Booking booking = doc.toObject(Booking.class);
                            booking.setBookingId(doc.getId()); // Lưu ID document để dùng cho việc Hủy
                            bookingList.add(booking);
                        }

                        // Cập nhật Adapter
                        if (bookingAdapter != null) {
                            bookingAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    // --- Logic Hủy phòng ---
    // Hàm này sẽ được gọi từ Adapter khi người dùng bấm nút Hủy
    public void cancelBooking(String bookingId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Chỉ cập nhật trạng thái thành "cancelled", KHÔNG XÓA document
        db.collection("bookings").document(bookingId)
                .update("status", "cancelled")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Đã hủy phòng thành công!", Toast.LENGTH_SHORT).show();
                    // SnapshotListener ở trên sẽ tự động reload lại giao diện
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi hủy phòng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}