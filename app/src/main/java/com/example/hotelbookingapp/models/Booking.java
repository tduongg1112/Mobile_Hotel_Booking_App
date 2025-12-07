package com.example.hotelbookingapp.models;

import com.google.firebase.Timestamp;

public class Booking {
    private String bookingId; // ID của document (để dùng khi hủy phòng)
    private String userId;
    private String hotelId;
    private String hotelName;
    private String hotelAddress;
    private String hotelImage;
    private String roomName;
    private String checkInDate;
    private String checkOutDate;
    private double totalPrice;
    private String status; // "confirmed", "cancelled"
    private Timestamp bookingDate;

    // Constructor rỗng bắt buộc cho Firestore
    public Booking() { }

    public Booking(String userId, String hotelId, String hotelName, String hotelAddress, String hotelImage, String roomName, String checkInDate, String checkOutDate, double totalPrice, String status, Timestamp bookingDate) {
        this.userId = userId;
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.hotelAddress = hotelAddress;
        this.hotelImage = hotelImage;
        this.roomName = roomName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalPrice = totalPrice;
        this.status = status;
        this.bookingDate = bookingDate;
    }

    // Getter & Setter
    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getUserId() { return userId; }
    public String getHotelId() { return hotelId; }
    public String getHotelName() { return hotelName; }
    public String getHotelAddress() { return hotelAddress; }
    public String getHotelImage() { return hotelImage; }
    public String getRoomName() { return roomName; }
    public String getCheckInDate() { return checkInDate; }
    public String getCheckOutDate() { return checkOutDate; }
    public double getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public Timestamp getBookingDate() { return bookingDate; }

    // Setter status để update khi hủy phòng
    public void setStatus(String status) { this.status = status; }
}