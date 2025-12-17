package com.example.hotelbookingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hotelbookingapp.R;
import com.example.hotelbookingapp.models.Review;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

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
        // Dùng layout item_review (CardView đẹp) của bạn
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        if (review == null) return;

        // 1. Tên
        holder.tvName.setText(review.getUserName() != null ? review.getUserName() : "Khách hàng");

        // 2. Nội dung
        holder.tvComment.setText(review.getComment());

        // 3. Rating (Model là float -> RatingBar nhận float OK)
        holder.ratingBar.setRating(review.getRating());

        // 4. Ngày tháng (QUAN TRỌNG: Sửa lỗi tại đây)
        if (review.getTimestamp() != null) {
            // Vì model dùng java.util.Date nên format trực tiếp
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            holder.tvDate.setText(sdf.format(review.getTimestamp()));
        } else {
            holder.tvDate.setText("");
        }

        // 5. Avatar (Tạo avatar ngẫu nhiên theo tên)
        String avatarUrl = "https://ui-avatars.com/api/?background=random&name=" + review.getUserName();
        Glide.with(context)
                .load(avatarUrl)
                .circleCrop()
                .placeholder(R.drawable.placeholder_image)
                .into(holder.imgAvatar);
    }

    @Override
    public int getItemCount() {
        return reviewList != null ? reviewList.size() : 0;
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView tvName, tvDate, tvComment;
        RatingBar ratingBar;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ đúng ID trong item_review.xml của bạn
            imgAvatar = itemView.findViewById(R.id.img_user_avatar);
            tvName = itemView.findViewById(R.id.tv_user_name);
            tvDate = itemView.findViewById(R.id.tv_review_date);
            tvComment = itemView.findViewById(R.id.tv_review_comment);
            ratingBar = itemView.findViewById(R.id.rating_bar_item);
        }
    }
}