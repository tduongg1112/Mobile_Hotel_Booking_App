package com.example.hotelbookingapp.models;

import java.util.List;

public class Room {
    private String id;
    private String roomName;
    private long price;
    private String description;
    private int capacity;
    private List<String> roomAmenities;

    // Constructor rỗng bắt buộc cho Firestore
    public Room() {
    }

    // --- Getters ---
    public String getId() {
        return id;
    }

    public String getRoomName() {
        return roomName;
    }

    public long getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<String> getRoomAmenities() {
        return roomAmenities;
    }

    // --- Setters ---
    public void setId(String id) {
        this.id = id;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setRoomAmenities(List<String> roomAmenities) {
        this.roomAmenities = roomAmenities;
    }
}