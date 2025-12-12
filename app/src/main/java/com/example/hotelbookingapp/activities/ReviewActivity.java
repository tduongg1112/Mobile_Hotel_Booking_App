package com.example.hotelbookingapp.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReviewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReviewAdapter adapter;
    private ReviewManager reviewManager;
    private String currentHotelId; // ID khách sạn nhận từ Intent

    // UI Components
    private TextView tvAvgRating, tvTotalReviews;
    private RatingBar ratingBarHeader;
    private ProgressBar prog5, prog4, prog3, prog2, prog1;
    private TextView tvPerc5, tvPerc4, tvPerc3, tvPerc2, tvPerc1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        // 1. Nhận dữ liệu từ màn hình trước (DetailActivity)
        if (getIntent().hasExtra("HOTEL_ID")) {
            currentHotelId = getIntent().getStringExtra("HOTEL_ID");
        } else {
            currentHotelId = "hotel_001"; // Mặc định nếu null (phòng hờ)
        }

        initViews();
        setupRecyclerView();

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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadReviewsFromFirebase() {
        // Gọi Manager để lấy dữ liệu thật
        reviewManager.getReviews(currentHotelId, new FirebaseCallback<List<Review>>() {
            @Override
            public void onSuccess(List<Review> result) {
                // Đổ dữ liệu vào Adapter
                adapter = new ReviewAdapter(result);
                recyclerView.setAdapter(adapter);

                // Tính toán thống kê thật
                calculateAndShowStats(result);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(ReviewActivity.this, "Lỗi tải review: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void calculateAndShowStats(List<Review> reviewList) {
        if (reviewList == null || reviewList.isEmpty()) {
            tvTotalReviews.setText("(0 đánh giá)");
            tvAvgRating.setText("0.0");
            return;
        }

        float totalRating = 0;
        int count5 = 0, count4 = 0, count3 = 0, count2 = 0, count1 = 0;

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

        int total = reviewList.size();
        float avg = totalRating / total;

        // Cập nhật Header
        tvAvgRating.setText(String.format(Locale.getDefault(), "%.1f", avg));
        ratingBarHeader.setRating(avg);
        tvTotalReviews.setText(String.format(Locale.getDefault(), "(%d đánh giá)", total));

        // Cập nhật biểu đồ
        updateProgressBar(prog5, tvPerc5, count5, total);
        updateProgressBar(prog4, tvPerc4, count4, total);
        updateProgressBar(prog3, tvPerc3, count3, total);
        updateProgressBar(prog2, tvPerc2, count2, total);
        updateProgressBar(prog1, tvPerc1, count1, total);
    }

    private void updateProgressBar(ProgressBar progressBar, TextView textView, int count, int total) {
        int percent = (total == 0) ? 0 : (int) (((float) count / total) * 100);
        progressBar.setProgress(percent);
        textView.setText(String.format(Locale.getDefault(), "%d%%", percent));
    }

    private void showAddReviewDialog() {
        // Kiểm tra đăng nhập trước khi cho viết review
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Vui lòng đăng nhập để viết đánh giá!", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_review, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        RatingBar ratingBar = view.findViewById(R.id.rating_bar_dialog);
        EditText edtComment = view.findViewById(R.id.edt_review_comment);
        Button btnSubmit = view.findViewById(R.id.btn_submit_review);

        btnSubmit.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            String comment = edtComment.getText().toString();

            if (rating == 0) {
                Toast.makeText(this, "Vui lòng chọn số sao!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo object Review thật
            Review newReview = new Review(
                    currentHotelId,
                    currentUser.getUid(),
                    currentUser.getDisplayName() != null ? currentUser.getDisplayName() : "Khách hàng",
                    rating,
                    comment,
                    new Date()
            );

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
}