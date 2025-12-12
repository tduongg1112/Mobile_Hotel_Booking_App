package com.example.hotelbookingapp.models;

<<<<<<< HEAD
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
=======
import com.google.firebase.firestore.PropertyName; // Import thư viện này
import java.io.Serializable;
import java.util.List;

public class Room implements Serializable { // Bắt buộc có Serializable để truyền giữa các Activity
    private String id;
    private String name;
    private long price;
    private int maxGuests;

    // Đánh dấu @PropertyName để Firestore hiểu chính xác trường này
    @PropertyName("isAvailable")
    private boolean isAvailable;

    private String image;
    private List<String> features;

    // Constructor rỗng bắt buộc cho Firestore
    public Room() { }

    // --- Getters & Setters ---

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public long getPrice() { return price; }
    public void setPrice(long price) { this.price = price; }

    public int getMaxGuests() { return maxGuests; }
    public void setMaxGuests(int maxGuests) { this.maxGuests = maxGuests; }

    // Lưu ý: Getter/Setter cho boolean có @PropertyName
    @PropertyName("isAvailable")
    public boolean isAvailable() { return isAvailable; }

    @PropertyName("isAvailable")
    public void setAvailable(boolean available) { isAvailable = available; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public List<String> getFeatures() { return features; }
    public void setFeatures(List<String> features) { this.features = features; }
>>>>>>> 45172f9a3310fc6720bdf3d0e0e59d8d1d28e484
}