package com.example.hotelbookingapp.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.PropertyName; // <--- CẦN IMPORT CÁI NÀY
import java.util.List;
import java.util.ArrayList;

public class Hotel implements Parcelable {
    private String id;
    private String name;
    private String location;
    private String address;
    private long minPrice;
    private float ratingAverage;
    private int ratingCount;
    private String description;
    private GeoPoint geoPoint;

    // --- KHÚC QUAN TRỌNG NHẤT ---
    // Lệnh này bảo Firebase: "Lấy dữ liệu từ trường 'imageUrl' trong DB đổ vào biến 'images' này"
    @PropertyName("imageUrl")
    private List<String> images;
    // -----------------------------

    private List<String> amenities;
    private List<String> searchKeywords;

    public Hotel() {}

    public Hotel(String id, String name, String location, String address, long minPrice, float ratingAverage, int ratingCount, String description, GeoPoint geoPoint, List<String> amenities, List<String> images, List<String> searchKeywords) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.address = address;
        this.minPrice = minPrice;
        this.ratingAverage = ratingAverage;
        this.ratingCount = ratingCount;
        this.description = description;
        this.geoPoint = geoPoint;
        this.amenities = amenities;
        this.images = images;
        this.searchKeywords = searchKeywords;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public long getMinPrice() { return minPrice; }
    public void setMinPrice(long minPrice) { this.minPrice = minPrice; }
    public float getRatingAverage() { return ratingAverage; }
    public void setRatingAverage(float ratingAverage) { this.ratingAverage = ratingAverage; }
    public int getRatingCount() { return ratingCount; }
    public void setRatingCount(int ratingCount) { this.ratingCount = ratingCount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public GeoPoint getGeoPoint() { return geoPoint; }
    public void setGeoPoint(GeoPoint geoPoint) { this.geoPoint = geoPoint; }
    public List<String> getAmenities() { return amenities; }
    public void setAmenities(List<String> amenities) { this.amenities = amenities; }
    public List<String> getSearchKeywords() { return searchKeywords; }
    public void setSearchKeywords(List<String> searchKeywords) { this.searchKeywords = searchKeywords; }

    // --- SỬA GETTER/SETTER CHO ẢNH ---
    // Phải thêm annotation vào cả Getter và Setter để nó hoạt động 2 chiều

    @PropertyName("imageUrl")
    public List<String> getImages() { return images; }

    @PropertyName("imageUrl")
    public void setImages(List<String> images) { this.images = images; }
    // ---------------------------------

    // Parcelable Implementation
    protected Hotel(Parcel in) {
        id = in.readString();
        name = in.readString();
        location = in.readString();
        address = in.readString();
        minPrice = in.readLong();
        ratingAverage = in.readFloat();
        ratingCount = in.readInt();
        description = in.readString();

        double lat = in.readDouble();
        double lng = in.readDouble();
        if (lat != 0 || lng != 0) { // Fix nhẹ logic check GeoPoint
            geoPoint = new GeoPoint(lat, lng);
        }

        // Đọc List
        amenities = in.createStringArrayList();
        images = in.createStringArrayList();
        searchKeywords = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(location);
        dest.writeString(address);
        dest.writeLong(minPrice);
        dest.writeFloat(ratingAverage);
        dest.writeInt(ratingCount);
        dest.writeString(description);

        if (geoPoint != null) {
            dest.writeDouble(geoPoint.getLatitude());
            dest.writeDouble(geoPoint.getLongitude());
        } else {
            dest.writeDouble(0);
            dest.writeDouble(0);
        }

        dest.writeStringList(amenities);
        dest.writeStringList(images);
        dest.writeStringList(searchKeywords);
    }

    @Override
    public int describeContents() { return 0; }

    public static final Creator<Hotel> CREATOR = new Creator<Hotel>() {
        @Override
        public Hotel createFromParcel(Parcel in) { return new Hotel(in); }
        @Override
        public Hotel[] newArray(int size) { return new Hotel[size]; }
    };
}