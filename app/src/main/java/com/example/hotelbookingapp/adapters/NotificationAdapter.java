package com.example.hotelbookingapp.adapters;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelbookingapp.R;
import com.example.hotelbookingapp.models.NotificationItem;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotiViewHolder> {

    private List<NotificationItem> notiList;

    public NotificationAdapter(List<NotificationItem> notiList) {
        this.notiList = notiList;
    }

    @NonNull
    @Override
    public NotiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotiViewHolder holder, int position) {
        NotificationItem item = notiList.get(position);
        if (item == null) return;

        holder.tvTitle.setText(item.getTitle());
        holder.tvBody.setText(item.getBody());

        // Format ngày giờ
        if (item.getTimestamp() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM", Locale.getDefault());
            holder.tvTime.setText(sdf.format(item.getTimestamp()));
        }

        String title = item.getTitle().toLowerCase();

        if (title.contains("khuyến mãi") || title.contains("voucher")) {
            // Icon Hộp quà - Nền Cam nhạt
            holder.imgIcon.setImageResource(R.drawable.ic_gift); // Nhớ tạo icon này
            holder.imgIcon.setColorFilter(Color.parseColor("#FF9800")); // Màu cam
            holder.cardIconContainer.setCardBackgroundColor(Color.parseColor("#FFF3E0")); // Nền cam nhạt

        } else if (title.contains("nhắc nhở") || title.contains("chuyến đi")) {
            // Icon Lịch - Nền Xanh dương nhạt
            holder.imgIcon.setImageResource(R.drawable.ic_calendar_check); // Nhớ tạo icon này
            holder.imgIcon.setColorFilter(Color.parseColor("#2196F3"));
            holder.cardIconContainer.setCardBackgroundColor(Color.parseColor("#E3F2FD"));

        } else if (title.contains("thành công") || title.contains("xác nhận")) {
            // Icon Tích xanh - Nền Xanh lá nhạt
            holder.imgIcon.setImageResource(R.drawable.ic_check_circle); // Nhớ tạo icon này
            holder.imgIcon.setColorFilter(Color.parseColor("#4CAF50"));
            holder.cardIconContainer.setCardBackgroundColor(Color.parseColor("#E8F5E9"));

        } else {
            // Mặc định: Icon Chuông - Nền Xám
            holder.imgIcon.setImageResource(R.drawable.ic_notification);
            holder.imgIcon.setColorFilter(Color.parseColor("#757575"));
            holder.cardIconContainer.setCardBackgroundColor(Color.parseColor("#F5F5F5"));
        }

        // Logic Đã đọc / Chưa đọc
        if (item.isRead()) {
            // Đã đọc: Ẩn chấm đỏ, chữ thường
            holder.dotUnread.setVisibility(View.GONE);
            holder.tvTitle.setTypeface(null, Typeface.NORMAL);
        } else {
            // Chưa đọc: Hiện chấm đỏ, chữ đậm
            holder.dotUnread.setVisibility(View.VISIBLE);
            holder.tvTitle.setTypeface(null, Typeface.BOLD);
        }
    }

    @Override
    public int getItemCount() {
        return notiList != null ? notiList.size() : 0;
    }

    public static class NotiViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvBody, tvTime;
        View dotUnread;
        ImageView imgIcon;
        CardView cardIconContainer;
        public NotiViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_noti_title);
            tvBody = itemView.findViewById(R.id.tv_noti_body);
            tvTime = itemView.findViewById(R.id.tv_noti_time);
            dotUnread = itemView.findViewById(R.id.view_unread_dot);
            imgIcon = itemView.findViewById(R.id.img_noti_icon);
            cardIconContainer = itemView.findViewById(R.id.card_icon_container);
        }
    }
}