package com.example.hotelbookingapp.models;

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
}