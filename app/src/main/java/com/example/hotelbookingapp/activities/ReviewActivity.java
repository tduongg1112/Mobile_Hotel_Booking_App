package com.example.hotelbookingapp.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.Date;
import java.util.List;

public class ReviewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReviewAdapter adapter;
    private ReviewManager reviewManager;
    private String currentHotelId = "hotel_001"; // ID ví dụ (Thực tế sẽ lấy từ Intent)
    private TextView tvAvgRating, tvTotalReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        // 1. Ánh xạ View
        recyclerView = findViewById(R.id.recycler_reviews);
        tvAvgRating = findViewById(R.id.tv_avg_rating);
        tvTotalReviews = findViewById(R.id.tv_total_reviews);
        FloatingActionButton btnAdd = findViewById(R.id.btn_add_review);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewManager = new ReviewManager();

        // 2. Nhận ID khách sạn từ màn hình trước (Nếu có)
        if (getIntent().hasExtra("HOTEL_ID")) {
            currentHotelId = getIntent().getStringExtra("HOTEL_ID");
        }

        // 3. Tải dữ liệu
        loadReviews();

        // 4. Xử lý nút Thêm Review
        btnAdd.setOnClickListener(v -> showAddReviewDialog());
    }

    private void loadReviews() {
        reviewManager.getReviews(currentHotelId, new FirebaseCallback<List<Review>>() {
            @Override
            public void onSuccess(List<Review> result) {
                // Đổ dữ liệu vào Adapter
                adapter = new ReviewAdapter(result);
                recyclerView.setAdapter(adapter);

                // Cập nhật thống kê sơ bộ
                tvTotalReviews.setText("(" + result.size() + " đánh giá)");
                if (!result.isEmpty()) {
                    float total = 0;
                    for (Review r : result) total += r.getRating();
                    tvAvgRating.setText(String.format("%.1f", total / result.size()));
                }
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(ReviewActivity.this, "Lỗi tải: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddReviewDialog() {
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

            // Tạo Review mới
            // LƯU Ý: Ở đây userId đang để cứng là "user_admin_999".
            // Sau này bạn thay bằng FirebaseAuth.getInstance().getCurrentUser().getUid()
            Review newReview = new Review(
                    currentHotelId,
                    "user_admin_999",
                    "Tôi (Admin)",
                    rating,
                    comment,
                    new Date()
            );

            // Gửi lên Firebase
            reviewManager.addReview(newReview, new FirebaseCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean result) {
                    Toast.makeText(ReviewActivity.this, "Đã gửi đánh giá!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    loadReviews(); // Tải lại danh sách để thấy review mới
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