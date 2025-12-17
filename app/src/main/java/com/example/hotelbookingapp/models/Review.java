package com.example.hotelbookingapp.models;

import java.util.Date;

public class Review {
    private String id;
    private String hotelId;
    private String userId;
    private String userName;
    private float rating;       // Dùng float để lưu 4.5 sao
    private String comment;
    private Date timestamp;     // Dùng Date chuẩn Java

    // 1. Constructor rỗng (Bắt buộc cho Firebase)
    public Review() {
    }

    // 2. Constructor đầy đủ
    public Review(String hotelId, String userId, String userName, float rating, String comment, Date timestamp) {
        this.hotelId = hotelId;
        this.userId = userId;
        this.userName = userName;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    // --- Getter & Setter ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getHotelId() { return hotelId; }
    public void setHotelId(String hotelId) { this.hotelId = hotelId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
}