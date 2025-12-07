package com.example.hotelbookingapp.firebase;

import android.util.Log;

import com.example.hotelbookingapp.models.Review;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

public class ReviewManager {
    private static final String TAG = "ReviewManager";
    private final FirebaseFirestore db;

    public ReviewManager() {
        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();
    }

    // 1. Hàm lấy danh sách Review của 1 khách sạn
    public void getReviews(String hotelId, FirebaseCallback<List<Review>> callback) {
        db.collection("reviews")
                .whereEqualTo("hotelId", hotelId)
                .orderBy("timestamp", Query.Direction.DESCENDING) // Mới nhất lên đầu
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null) {
                        // Tự động chuyển dữ liệu JSON thành List<Review>
                        List<Review> reviewList = queryDocumentSnapshots.toObjects(Review.class);
                        callback.onSuccess(reviewList);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Lỗi lấy review: ", e);
                    callback.onFailure(e.getMessage());
                });
    }

    // 2. Hàm gửi Review mới
    public void addReview(Review review, FirebaseCallback<Boolean> callback) {
        // Tạo document mới trong collection 'reviews'
        db.collection("reviews")
                .add(review)
                .addOnSuccessListener(documentReference -> {
                    // Nếu thành công, update lại ID cho object review (nếu cần)
                    String docId = documentReference.getId();
                    review.setId(docId);
                    callback.onSuccess(true);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Lỗi thêm review: ", e);
                    callback.onFailure(e.getMessage());
                });
    }
}