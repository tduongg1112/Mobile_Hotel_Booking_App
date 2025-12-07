package com.example.hotelbookingapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelbookingapp.R;
import com.example.hotelbookingapp.adapters.NotificationAdapter;
import com.example.hotelbookingapp.firebase.FirebaseCallback;
import com.example.hotelbookingapp.firebase.NotificationManager;
import com.example.hotelbookingapp.models.NotificationItem;

import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerNoti;
    private NotificationManager notiManager;
    private ImageView btnBack;
    private TextView tvEmpty; // Hiện khi không có thông báo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        recyclerNoti = findViewById(R.id.recycler_notifications);
        btnBack = findViewById(R.id.btn_back_noti);
        tvEmpty = findViewById(R.id.tv_empty_noti);

        recyclerNoti.setLayoutManager(new LinearLayoutManager(this));
        notiManager = new NotificationManager();

        btnBack.setOnClickListener(v -> finish()); // Quay lại

        loadNotifications();
    }

    private void loadNotifications() {
        // LƯU Ý: Thay ID này bằng ID thật của User đang đăng nhập
        String currentUserId = "user_review_0001";

        notiManager.getNotifications(currentUserId, new FirebaseCallback<List<NotificationItem>>() {
            @Override
            public void onSuccess(List<NotificationItem> result) {
                if (result.isEmpty()) {
                    tvEmpty.setVisibility(View.VISIBLE);
                    recyclerNoti.setVisibility(View.GONE);
                } else {
                    tvEmpty.setVisibility(View.GONE);
                    recyclerNoti.setVisibility(View.VISIBLE);
                    NotificationAdapter adapter = new NotificationAdapter(result);
                    recyclerNoti.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(NotificationActivity.this, "Lỗi: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}