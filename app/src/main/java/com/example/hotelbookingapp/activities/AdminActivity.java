package com.example.hotelbookingapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelbookingapp.R;
import com.example.hotelbookingapp.models.NotificationItem;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Date;

public class AdminActivity extends AppCompatActivity {

    private EditText etPromoTitle, etPromoBody;
    private Button btnSendPromo;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        db = FirebaseFirestore.getInstance();

        etPromoTitle = findViewById(R.id.et_promo_title);
        etPromoBody = findViewById(R.id.et_promo_body);
        btnSendPromo = findViewById(R.id.btn_send_promo);

        btnSendPromo.setOnClickListener(v -> sendPromotionalNotifications());
    }

    private void sendPromotionalNotifications() {
        String title = etPromoTitle.getText().toString().trim();
        String body = etPromoBody.getText().toString().trim();

        if (title.isEmpty() || body.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ tiêu đề và nội dung.", Toast.LENGTH_SHORT).show();
            return;
        }

        btnSendPromo.setEnabled(false);
        Toast.makeText(this, "Đang gửi thông báo...", Toast.LENGTH_SHORT).show();

        db.collection("users")
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                for (QueryDocumentSnapshot userDocument : queryDocumentSnapshots) {
                    String userId = userDocument.getId();
                    NotificationItem notification = new NotificationItem(userId, title, body, new Date());

                    db.collection("notifications").add(notification)
                        .addOnSuccessListener(documentReference -> Log.d("AdminActivity", "Notification sent to user: " + userId))
                        .addOnFailureListener(e -> Log.e("AdminActivity", "Failed to send notification to user: " + userId, e));
                }
                Toast.makeText(AdminActivity.this, "Đã gửi thông báo khuyến mãi cho tất cả người dùng.", Toast.LENGTH_LONG).show();
                btnSendPromo.setEnabled(true);
                finish();
            })
            .addOnFailureListener(e -> {
                Toast.makeText(AdminActivity.this, "Lỗi khi lấy danh sách người dùng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                btnSendPromo.setEnabled(true);
            });
    }
}
