package com.example.hotelbookingapp.firebase;

import com.example.hotelbookingapp.models.NotificationItem;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

public class NotificationManager {
    private static final String TAG = "NotificationManager";
    private final FirebaseFirestore db;

    public NotificationManager() {
        db = FirebaseFirestore.getInstance();
    }

    // Lấy thông báo của user hiện tại
    public void getNotifications(String userId, FirebaseCallback<List<NotificationItem>> callback) {
        db.collection("notifications") // (Lưu ý: Collection này sẽ tự tạo khi bạn push noti)
                .whereEqualTo("userId", userId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(snapshots -> {
                    List<NotificationItem> list = snapshots.toObjects(NotificationItem.class);
                    callback.onSuccess(list);
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage());
                });
    }
}