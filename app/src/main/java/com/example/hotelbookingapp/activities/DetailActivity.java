package com.example.hotelbookingapp.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.hotelbookingapp.R;
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

    private ImageView imgHeader, btnBack;
    private TextView tvName, tvLocation, tvDescription, tvRating;
    private RecyclerView rvRooms, rvReviews;

    private RoomAdapter roomAdapter;
    private ReviewAdapter reviewAdapter;
    private List<Room> roomList;
    private List<Review> reviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        db = FirebaseFirestore.getInstance();

        if (getIntent().hasExtra("hotel_object")) {
            currentHotel = getIntent().getParcelableExtra("hotel_object");
        }

        if (currentHotel == null) {
            Toast.makeText(this, "Lỗi dữ liệu", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        displayHotelInfo();
        fetchRooms(currentHotel.getId());
        fetchReviews(currentHotel.getId());

        btnBack.setOnClickListener(v -> finish());
    }

    private void initViews() {
        imgHeader = findViewById(R.id.img_detail_header);
        tvName = findViewById(R.id.tv_detail_name);
        tvLocation = findViewById(R.id.tv_detail_location);
        tvDescription = findViewById(R.id.tv_detail_description);
        tvRating = findViewById(R.id.tv_detail_rating);
        btnBack = findViewById(R.id.btn_back);
        rvRooms = findViewById(R.id.rv_rooms);
        rvReviews = findViewById(R.id.rv_reviews);

        // Setup Room Recycler
        rvRooms.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        roomList = new ArrayList<>();
        // TRUYỀN ID KHÁCH SẠN VÀO ĐÂY ĐỂ ADAPTER BIẾT TÌM ẢNH
        roomAdapter = new RoomAdapter(this, roomList, currentHotel);
        rvRooms.setAdapter(roomAdapter);

        // Setup Review Recycler
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

        // --- LOGIC ẢNH HEADER (Ưu tiên Local) ---
        int resId = getResources().getIdentifier(
                currentHotel.getId(), "drawable", getPackageName());

        if (resId != 0) {
            // Có ảnh máy -> Hiện luôn
            imgHeader.setImageResource(resId);
        } else {
            // Không có -> Tải mạng
            String urlToLoad = null;
            if (currentHotel.getImageUrl() != null && !currentHotel.getImageUrl().isEmpty()) {
                urlToLoad = currentHotel.getImageUrl();
            } else if (currentHotel.getImages() != null && !currentHotel.getImages().isEmpty()) {
                urlToLoad = currentHotel.getImages().get(0);
            }

            if (urlToLoad != null) {
                GlideUrl glideUrl = new GlideUrl(urlToLoad, new LazyHeaders.Builder()
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36")
                        .build());
                Glide.with(this).load(glideUrl)
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image)
                        .into(imgHeader);
            } else {
                imgHeader.setImageResource(R.drawable.placeholder_image);
            }
        }
    }

    private void fetchRooms(String hotelId) {
        db.collection("hotels").document(hotelId).collection("rooms")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    roomList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Room room = doc.toObject(Room.class);
                        room.setId(doc.getId()); // Cần ID này để ghép tên ảnh (dlx, std...)
                        roomList.add(room);
                    }
                    roomAdapter.notifyDataSetChanged();
                });
    }

    private void fetchReviews(String hotelId) {
        db.collection("reviews")
                .whereEqualTo("hotelId", hotelId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    reviewList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Review review = doc.toObject(Review.class);
                        reviewList.add(review);
                    }
                    reviewAdapter.notifyDataSetChanged();
                });
    }
}