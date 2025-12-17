package com.example.hotelbookingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> reviewList;

    // Constructor nhận dữ liệu
    public ReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Dùng layout item_review (CardView đẹp) của bạn
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        // Gọi file layout item_review.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        if (review == null) return;

        // 1. Tên
        holder.tvName.setText(review.getUserName() != null ? review.getUserName() : "Khách hàng");

        // 2. Nội dung
        // 1. Gán Tên & Comment
        holder.tvName.setText(review.getUserName());
        holder.tvComment.setText(review.getComment());

        // 3. Rating (Model là float -> RatingBar nhận float OK)
        holder.ratingBar.setRating(review.getRating());
        // 2. Gán Số sao
        holder.ratingBar.setRating(review.getRating());

        // 4. Ngày tháng (QUAN TRỌNG: Sửa lỗi tại đây)
        // 3. Xử lý Ngày tháng (Format Date sang String đẹp)
        if (review.getTimestamp() != null) {
            // Vì model dùng java.util.Date nên format trực tiếp
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            holder.tvDate.setText(sdf.format(review.getTimestamp()));
        } else {
            holder.tvDate.setText("");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            holder.tvDate.setText(sdf.format(review.getTimestamp()));
        }

        // 5. Avatar (Tạo avatar ngẫu nhiên theo tên)
        String avatarUrl = "https://ui-avatars.com/api/?background=random&name=" + review.getUserName();
        Glide.with(context)
                .load(avatarUrl)
                .circleCrop()
                .placeholder(R.drawable.placeholder_image)
                .into(holder.imgAvatar);

        // 4. Load Avatar giả lập (Vì Database ta chưa có link avatar thật)
        // Dùng Glide để load một ảnh ngẫu nhiên hoặc ảnh mặc định
        Glide.with(holder.itemView.getContext())
                .load("https://i.pravatar.cc/150?u=" + review.getUserName()) // API tạo avatar ngẫu nhiên theo tên
                .placeholder(R.drawable.ic_launcher_background) // Ảnh chờ
                .into(holder.imgAvatar);
    }

    @Override
    public int getItemCount() {
        return reviewList != null ? reviewList.size() : 0;
    }

    // Class nắm giữ các thành phần giao diện
    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView tvName, tvDate, tvComment;
        RatingBar ratingBar;
        CircleImageView imgAvatar;
        TextView tvName, tvDate, tvComment;
        RatingBar ratingBar;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ đúng ID trong item_review.xml của bạn
            imgAvatar = itemView.findViewById(R.id.img_user_avatar);
            tvName = itemView.findViewById(R.id.tv_user_name);
            imgAvatar = itemView.findViewById(R.id.img_user_avatar);
            tvName = itemView.findViewById(R.id.tv_user_name);
            tvDate = itemView.findViewById(R.id.tv_review_date);
            tvComment = itemView.findViewById(R.id.tv_review_comment);
            ratingBar = itemView.findViewById(R.id.rating_bar_item);
        }
    }
}