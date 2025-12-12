package com.example.hotelbookingapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hotelbookingapp.R; // Import R của dự án
import com.example.hotelbookingapp.adapters.ReviewAdapter;
import com.example.hotelbookingapp.adapters.RoomAdapter;
import com.example.hotelbookingapp.models.Hotel;
import com.example.hotelbookingapp.models.Review;
import com.example.hotelbookingapp.models.Room;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private Hotel currentHotel;

    // Views
    private ImageView imgHeader, btnBack;
    private TextView tvName, tvLocation, tvDescription, tvRating;
    private RecyclerView rvRooms, rvReviews;

    // Adapters & Lists
    private RoomAdapter roomAdapter;
    private ReviewAdapter reviewAdapter;
    private List<Room> roomList;
    private List<Review> reviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        db = FirebaseFirestore.getInstance();
        initViews();

        // Nhận dữ liệu
        if (getIntent().hasExtra("hotel_object")) {
            currentHotel = getIntent().getParcelableExtra("hotel_object");
        }

        if (currentHotel != null) {
            displayHotelInfo();
            fetchRooms(currentHotel.getId());
            fetchReviews(currentHotel.getId());
        } else {
            Toast.makeText(this, "Lỗi: Không tải được thông tin", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnBack.setOnClickListener(v -> finish());
    }

    private void initViews() {
        // Các ID này phải có trong file activity_detail.xml bên dưới
        imgHeader = findViewById(R.id.img_detail_header);
        tvName = findViewById(R.id.tv_detail_name);
        tvLocation = findViewById(R.id.tv_detail_location);
        tvDescription = findViewById(R.id.tv_detail_description);
        tvRating = findViewById(R.id.tv_detail_rating);
        btnBack = findViewById(R.id.btn_back);

        rvRooms = findViewById(R.id.rv_rooms);
        rvReviews = findViewById(R.id.rv_reviews);

        // Setup Rooms (Horizontal)
        rvRooms.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        roomList = new ArrayList<>();
        roomAdapter = new RoomAdapter(this, roomList);
        rvRooms.setAdapter(roomAdapter);

        // Setup Reviews (Vertical)
        rvReviews.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        reviewList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(this, reviewList);
        rvReviews.setAdapter(reviewAdapter);
    }

    private void displayHotelInfo() {
        tvName.setText(currentHotel.getName());
        tvLocation.setText(currentHotel.getAddress());
        tvDescription.setText(currentHotel.getDescription());
        tvRating.setText(String.valueOf(currentHotel.getRatingAverage()));

        if (currentHotel.getImages() != null && !currentHotel.getImages().isEmpty()) {
            Glide.with(this)
                    .load(currentHotel.getImages().get(0))
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(imgHeader);
        }
    }

    private void fetchRooms(String hotelId) {
        db.collection("hotels").document(hotelId)
                .collection("rooms")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    roomList.clear();
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            Room room = doc.toObject(Room.class);
                            // room.setId(doc.getId());
                            roomList.add(room);
                        }
                        roomAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> Log.e("DetailActivity", "Lỗi load phòng: ", e));
    }

    private void fetchReviews(String hotelId) {
        db.collection("reviews")
                .whereEqualTo("hotelId", hotelId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    reviewList.clear();
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            Review review = doc.toObject(Review.class);
                            reviewList.add(review);
                        }
                        reviewAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> Log.e("DetailActivity", "Lỗi load review: ", e));
    }
}