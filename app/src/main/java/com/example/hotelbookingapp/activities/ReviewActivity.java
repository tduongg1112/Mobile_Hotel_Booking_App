package com.example.hotelbookingapp.activities;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelbookingapp.R;
import com.example.hotelbookingapp.adapters.ReviewAdapter;
import com.example.hotelbookingapp.firebase.FirebaseCallback;
import com.example.hotelbookingapp.firebase.ReviewManager;
import com.example.hotelbookingapp.models.Review;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date; // QUAN TRỌNG: Import java.util.Date
import java.util.List;
import java.util.Locale;

public class ReviewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReviewAdapter adapter;
    private List<Review> reviewList;
    private FirebaseFirestore db;
    private String currentHotelId;
    private ReviewManager reviewManager;
    private String currentHotelId; // ID khách sạn nhận từ Intent

    // Header thống kê
    // UI Components
    private TextView tvAvgRating, tvTotalReviews;
    private RatingBar ratingBarHeader;
    private ProgressBar prog5, prog4, prog3, prog2, prog1;
    private TextView tvPerc5, tvPerc4, tvPerc3, tvPerc2, tvPerc1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        db = FirebaseFirestore.getInstance();

        // 1. Nhận dữ liệu từ màn hình trước (DetailActivity)
        if (getIntent().hasExtra("HOTEL_ID")) {
            currentHotelId = getIntent().getStringExtra("HOTEL_ID");
        } else {
            currentHotelId = "hotel_001";
            currentHotelId = "hotel_001"; // Mặc định nếu null (phòng hờ)
        }

        initViews();
        setupRecyclerView();
        loadReviews();

        // Nút thêm review -> Kiểm tra booking trước
        findViewById(R.id.btn_add_review).setOnClickListener(v -> checkBookingAndReview());
        reviewManager = new ReviewManager();

        // 2. Tải dữ liệu thật từ Firebase
        loadReviewsFromFirebase();

        // 3. Xử lý nút Thêm Review
        findViewById(R.id.btn_add_review).setOnClickListener(v -> showAddReviewDialog());

        // 4. Xử lý nút Back
        findViewById(R.id.btn_back_review).setOnClickListener(v -> finish());
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_reviews);
        tvAvgRating = findViewById(R.id.tv_avg_rating);
        tvTotalReviews = findViewById(R.id.tv_total_reviews);
        ratingBarHeader = findViewById(R.id.rating_bar_header);

        // Biểu đồ
        prog5 = findViewById(R.id.progress_5_star);
        prog4 = findViewById(R.id.progress_4_star);
        prog3 = findViewById(R.id.progress_3_star);
        prog2 = findViewById(R.id.progress_2_star);
        prog1 = findViewById(R.id.progress_1_star);

        tvPerc5 = findViewById(R.id.tv_percent_5_star);
        tvPerc4 = findViewById(R.id.tv_percent_4_star);
        tvPerc3 = findViewById(R.id.tv_percent_3_star);
        tvPerc2 = findViewById(R.id.tv_percent_2_star);
        tvPerc1 = findViewById(R.id.tv_percent_1_star);
    }

    private void setupRecyclerView() {
        reviewList = new ArrayList<>();
        adapter = new ReviewAdapter(this, reviewList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    }

    private void loadReviewsFromFirebase() {
        // Gọi Manager để lấy dữ liệu thật
        reviewManager.getReviews(currentHotelId, new FirebaseCallback<List<Review>>() {
            @Override
            public void onSuccess(List<Review> result) {
                // Đổ dữ liệu vào Adapter
                adapter = new ReviewAdapter(result);
                recyclerView.setAdapter(adapter);

    private void loadReviews() {
        db.collection("reviews")
                .whereEqualTo("hotelId", currentHotelId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) return;
                    if (snapshots != null) {
                        reviewList.clear();
                        for (QueryDocumentSnapshot doc : snapshots) {
                            // Firebase tự convert Timestamp về Date nếu tên field khớp
                            Review review = doc.toObject(Review.class);
                            reviewList.add(review);
                        }
                        adapter.notifyDataSetChanged();
                        calculateAndShowStats(reviewList);
                    }
                });
                // Tính toán thống kê thật
                calculateAndShowStats(result);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(ReviewActivity.this, "Lỗi tải review: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void calculateAndShowStats(List<Review> list) {
        if (list == null || list.isEmpty()) {
    private void calculateAndShowStats(List<Review> reviewList) {
        if (reviewList == null || reviewList.isEmpty()) {
            tvTotalReviews.setText("(0 đánh giá)");
            tvAvgRating.setText("0.0");
            ratingBarHeader.setRating(0);
            updateProgressBar(0, 0, 0, 0, 0, 0);
            return;
        }

        float totalRating = 0;
        int c5 = 0, c4 = 0, c3 = 0, c2 = 0, c1 = 0;
        int count5 = 0, count4 = 0, count3 = 0, count2 = 0, count1 = 0;

        for (Review r : list) {
            totalRating += r.getRating();
            int star = Math.round(r.getRating());
            if (star == 5) c5++;
            else if (star == 4) c4++;
            else if (star == 3) c3++;
            else if (star == 2) c2++;
            else if (star == 1) c1++;
        for (Review r : reviewList) {
            float rating = r.getRating();
            totalRating += rating;
            int star = Math.round(rating);
            if (star == 5) count5++;
            else if (star == 4) count4++;
            else if (star == 3) count3++;
            else if (star == 2) count2++;
            else if (star == 1) count1++;
        }

        int total = list.size();
        int total = reviewList.size();
        float avg = totalRating / total;

        // Cập nhật Header
        tvAvgRating.setText(String.format(Locale.getDefault(), "%.1f", avg));
        ratingBarHeader.setRating(avg);
        tvTotalReviews.setText(String.format(Locale.getDefault(), "(%d đánh giá)", total));

        updateProgressBar(total, c5, c4, c3, c2, c1);

        // Cập nhật rating trung bình vào bảng hotel (nếu cần)
        updateHotelRating(avg, total);
        // Cập nhật biểu đồ
        updateProgressBar(prog5, tvPerc5, count5, total);
        updateProgressBar(prog4, tvPerc4, count4, total);
        updateProgressBar(prog3, tvPerc3, count3, total);
        updateProgressBar(prog2, tvPerc2, count2, total);
        updateProgressBar(prog1, tvPerc1, count1, total);
    }

    private void updateProgressBar(int total, int c5, int c4, int c3, int c2, int c1) {
        setBar(prog5, tvPerc5, c5, total);
        setBar(prog4, tvPerc4, c4, total);
        setBar(prog3, tvPerc3, c3, total);
        setBar(prog2, tvPerc2, c2, total);
        setBar(prog1, tvPerc1, c1, total);
    }

    private void setBar(ProgressBar pb, TextView tv, int count, int total) {
    private void updateProgressBar(ProgressBar progressBar, TextView textView, int count, int total) {
        int percent = (total == 0) ? 0 : (int) (((float) count / total) * 100);
        pb.setProgress(percent);
        tv.setText(percent + "%");
        progressBar.setProgress(percent);
        textView.setText(String.format(Locale.getDefault(), "%d%%", percent));
    }

    private void checkBookingAndReview() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Vui lòng đăng nhập!", Toast.LENGTH_SHORT).show();
    private void showAddReviewDialog() {
        // Kiểm tra đăng nhập trước khi cho viết review
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Vui lòng đăng nhập để viết đánh giá!", Toast.LENGTH_SHORT).show();
            return;
        }

        View btnAdd = findViewById(R.id.btn_add_review);
        btnAdd.setEnabled(false);

        db.collection("bookings")
                .whereEqualTo("userId", user.getUid())
                .whereEqualTo("hotelId", currentHotelId)
                .get()
                .addOnSuccessListener(snapshots -> {
                    btnAdd.setEnabled(true);

                    boolean hasConfirmedBooking = false;
                    for (DocumentSnapshot doc : snapshots) {
                        String status = doc.getString("status");
                        if ("CONFIRMED".equals(status)) {
                            hasConfirmedBooking = true;
                            break;
                        }
                    }

                    if (hasConfirmedBooking) {
                        showAddReviewDialog(user);
                    } else {
                        new AlertDialog.Builder(this)
                                .setTitle("Chưa thể đánh giá")
                                .setMessage("Bạn cần đặt phòng thành công tại khách sạn này mới được viết đánh giá.")
                                .setPositiveButton("Đã hiểu", null)
                                .show();
                    }
                })
                .addOnFailureListener(e -> {
                    btnAdd.setEnabled(true);
                    Toast.makeText(this, "Lỗi kiểm tra: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void showAddReviewDialog(FirebaseUser user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_review, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        RatingBar ratingBar = view.findViewById(R.id.rating_bar_dialog);
        EditText edtComment = view.findViewById(R.id.edt_review_comment);
        Button btnSubmit = view.findViewById(R.id.btn_submit_review);

        btnSubmit.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            String comment = edtComment.getText().toString().trim();
            String comment = edtComment.getText().toString();

            if (rating == 0) {
                Toast.makeText(this, "Vui lòng chọn số sao!", Toast.LENGTH_SHORT).show();
                return;
            }

            // --- TẠO OBJECT REVIEW (Dùng Constructor của bạn) ---
            String displayName = user.getDisplayName();
            if (displayName == null || displayName.isEmpty()) displayName = user.getEmail();

            // Tạo object Review thật
            Review newReview = new Review(
                    currentHotelId,
                    user.getUid(),
                    displayName,
                    currentUser.getUid(),
                    currentUser.getDisplayName() != null ? currentUser.getDisplayName() : "Khách hàng",
                    rating,
                    comment,
                    new Date() // Sử dụng java.util.Date
                    new Date()
            );

            db.collection("reviews").add(newReview)
                    .addOnSuccessListener(doc -> {
                        Toast.makeText(this, "Gửi đánh giá thành công!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Gửi thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
            // Gửi lên Firebase thật
            reviewManager.addReview(newReview, new FirebaseCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean result) {
                    Toast.makeText(ReviewActivity.this, "Gửi thành công!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    loadReviewsFromFirebase(); // Tải lại danh sách để thấy review mới ngay lập tức
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(ReviewActivity.this, "Lỗi: " + message, Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialog.show();
    }

    private void updateHotelRating(float newAvg, int newCount) {
        // Cập nhật lại thông tin vào bảng Hotel để hiển thị ngoài trang chủ
        db.collection("hotels").document(currentHotelId)
                .update("ratingAverage", newAvg, "ratingCount", newCount);
    }
}