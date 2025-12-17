package com.example.hotelbookingapp.models;

import com.google.firebase.Timestamp;

public class Booking {
    private String bookingId;
    private String userId;
    private String hotelId;
    private String hotelName;
    private String hotelAddress;
    private String hotelImage;
    private String roomName;
    private String checkInDate;
    private String checkOutDate;
    private double totalPrice;
    private String status; // "CONFIRMED", "PENDING"
    private Timestamp timestamp;

    public Booking() {}

    public Booking(String userId, String hotelId, String hotelName, String hotelAddress, String hotelImage, String roomName, String checkInDate, String checkOutDate, double totalPrice, String status, Timestamp timestamp) {
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
        this.timestamp = timestamp;
    }

    // --- Getter và Setter (Bắt buộc phải có để Firestore đọc/ghi) ---
    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getHotelId() { return hotelId; }
    public void setHotelId(String hotelId) { this.hotelId = hotelId; }

    public String getHotelName() { return hotelName; }
    public void setHotelName(String hotelName) { this.hotelName = hotelName; }

    public String getHotelAddress() { return hotelAddress; }
    public void setHotelAddress(String hotelAddress) { this.hotelAddress = hotelAddress; }

    public String getHotelImage() { return hotelImage; }
    public void setHotelImage(String hotelImage) { this.hotelImage = hotelImage; }

    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }

    public String getCheckInDate() { return checkInDate; }
    public void setCheckInDate(String checkInDate) { this.checkInDate = checkInDate; }

    public String getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(String checkOutDate) { this.checkOutDate = checkOutDate; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getTimestamp() { return timestamp; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }
}