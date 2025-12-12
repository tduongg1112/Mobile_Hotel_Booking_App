package com.example.hotelbookingapp.adapters;

import android.content.Context;
<<<<<<< HEAD
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hotelbookingapp.models.Review; // Nhớ có model Review
=======
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hotelbookingapp.R;
import com.example.hotelbookingapp.models.Review;
>>>>>>> 45172f9a3310fc6720bdf3d0e0e59d8d1d28e484
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private Context context;
    private List<Review> reviewList;

    public ReviewAdapter(Context context, List<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
<<<<<<< HEAD
        View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
=======
        // Sử dụng layout item_review vừa tạo ở Bước 1
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
>>>>>>> 45172f9a3310fc6720bdf3d0e0e59d8d1d28e484
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
<<<<<<< HEAD
        // Logic bind data
=======
        Review review = reviewList.get(position);

        // Hiển thị tên người dùng
        holder.tvUsername.setText(review.getUserName() != null ? review.getUserName() : "Ẩn danh");

        // Hiển thị comment
        holder.tvComment.setText(review.getComment());

        // Hiển thị rating
        holder.tvRating.setText(review.getRating() + " ★");

        // Hiển thị ngày tháng (nếu có timestamp)
        if (review.getTimestamp() != null) {
            String date = DateFormat.format("dd/MM/yyyy", review.getTimestamp().toDate()).toString();
            holder.tvDate.setText(date);
        } else {
            holder.tvDate.setText("");
        }
>>>>>>> 45172f9a3310fc6720bdf3d0e0e59d8d1d28e484
    }

    @Override
    public int getItemCount() {
        return reviewList != null ? reviewList.size() : 0;
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
<<<<<<< HEAD
        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
=======
        TextView tvUsername, tvDate, tvRating, tvComment;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ ID từ file item_review.xml
            tvUsername = itemView.findViewById(R.id.tv_review_username);
            tvDate = itemView.findViewById(R.id.tv_review_date);
            tvRating = itemView.findViewById(R.id.tv_review_rating);
            tvComment = itemView.findViewById(R.id.tv_review_comment);
>>>>>>> 45172f9a3310fc6720bdf3d0e0e59d8d1d28e484
        }
    }
}