package com.example.hotelbookingapp.models;

import com.google.firebase.Timestamp;

public class Review {
    private String hotelId;
    private String userId;
    private String userName;
    private int rating;
    private String comment;
    private Timestamp timestamp;

    // Constructor rỗng bắt buộc cho Firestore
    public Review() {
    }

    // --- Getters ---
    public String getHotelId() {
        return hotelId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    // --- Setters ---
    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}