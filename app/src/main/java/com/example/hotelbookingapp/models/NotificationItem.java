package com.example.hotelbookingapp.models;

import java.util.Date;

public class NotificationItem {
    private String id;
    private String userId;      // Thông báo này gửi cho ai
    private String title;       // Tiêu đề: "Khuyến mãi", "Nhắc nhở"
    private String body;        // Nội dung: "Bạn có mã giảm giá...", "Sắp đến giờ checkin"
    private boolean isRead;     // Đã đọc chưa (true/false)
    private Date timestamp;     // Thời gian nhận

    // Constructor rỗng (Bắt buộc)
    public NotificationItem() {
    }

    // Constructor đầy đủ
    public NotificationItem(String userId, String title, String body, Date timestamp) {
        this.userId = userId;
        this.title = title;
        this.body = body;
        this.timestamp = timestamp;
        this.isRead = false; // Mặc định là chưa đọc
    }

    // --- Getter & Setter ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
}