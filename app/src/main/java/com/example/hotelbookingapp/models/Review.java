package com.example.hotelbookingapp.models;

import java.util.Date;

public class Review {
    private String id;          // ID của document (để sau này xóa/sửa)
    private String hotelId;     // Review này thuộc khách sạn nào
    private String userId;      // ID người dùng
    private String userName;    // Tên người dùng (Lưu cứng để đỡ phải query lại bảng User)
    private float rating;       // Số sao (1.0 - 5.0)
    private String comment;     // Nội dung đánh giá
    private Date timestamp;     // Thời gian đăng (Dùng java.util.Date cho dễ xử lý)

    // ⚠️ QUAN TRỌNG: Phải có Constructor rỗng để Firestore map dữ liệu về
    public Review() {
    }

    // Constructor đầy đủ (Dùng khi bạn tạo Review mới để đẩy lên)
    public Review(String hotelId, String userId, String userName, float rating, String comment, Date timestamp) {
        this.hotelId = hotelId;
        this.userId = userId;
        this.userName = userName;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    // --- Getter & Setter (Bắt buộc) ---
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