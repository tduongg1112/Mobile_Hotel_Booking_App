package com.example.hotelbookingapp.firebase;

public interface FirebaseCallback<T> {
    void onSuccess(T result);      // Khi tải thành công, trả về dữ liệu kiểu T
    void onFailure(String message); // Khi lỗi, trả về dòng thông báo lỗi
}