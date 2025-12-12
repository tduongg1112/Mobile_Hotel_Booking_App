package com.example.hotelbookingapp.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hotelbookingapp.R;
import com.example.hotelbookingapp.models.Review;
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
        // Sử dụng layout item_review vừa tạo ở Bước 1
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
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
    }

    @Override
    public int getItemCount() {
        return reviewList != null ? reviewList.size() : 0;
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvDate, tvRating, tvComment;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ ID từ file item_review.xml
            tvUsername = itemView.findViewById(R.id.tv_review_username);
            tvDate = itemView.findViewById(R.id.tv_review_date);
            tvRating = itemView.findViewById(R.id.tv_review_rating);
            tvComment = itemView.findViewById(R.id.tv_review_comment);
        }
    }
}