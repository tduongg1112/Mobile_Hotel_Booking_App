package com.example.hotelbookingapp.adapters;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

        public NotiViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_noti_title);
            tvBody = itemView.findViewById(R.id.tv_noti_body);
            tvTime = itemView.findViewById(R.id.tv_noti_time);
            dotUnread = itemView.findViewById(R.id.view_unread_dot);
            imgIcon = itemView.findViewById(R.id.img_noti_icon);
        }
    }
}