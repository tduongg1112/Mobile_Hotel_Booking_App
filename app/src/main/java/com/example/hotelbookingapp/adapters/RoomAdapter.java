package com.example.hotelbookingapp.adapters;

import android.content.Context;
<<<<<<< HEAD
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hotelbookingapp.R;
import com.example.hotelbookingapp.models.Room; // Nhớ có model Room
import java.util.List;
=======
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button; // Nhớ import Button
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.hotelbookingapp.R;
import com.example.hotelbookingapp.activities.RoomDetailActivity; // Chuyển hướng sang đây
import com.example.hotelbookingapp.models.Hotel; // Import Hotel
import com.example.hotelbookingapp.models.Room;
import com.example.hotelbookingapp.utils.GlideUtils; // Nếu có dùng

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
>>>>>>> 45172f9a3310fc6720bdf3d0e0e59d8d1d28e484

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private Context context;
    private List<Room> roomList;
<<<<<<< HEAD

    public RoomAdapter(Context context, List<Room> roomList) {
        this.context = context;
        this.roomList = roomList;
=======
    private Hotel currentHotel; // Đổi từ String sang Hotel Object để truyền dữ liệu đầy đủ

    // Cập nhật Constructor nhận Hotel Object
    public RoomAdapter(Context context, List<Room> roomList, Hotel hotel) {
        this.context = context;
        this.roomList = roomList;
        this.currentHotel = hotel;
>>>>>>> 45172f9a3310fc6720bdf3d0e0e59d8d1d28e484
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
<<<<<<< HEAD
        // Tạm thời dùng layout simple_list_item_1 của android nếu chưa có layout item_room
        View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
=======
        View view = LayoutInflater.from(context).inflate(R.layout.item_room_detail, parent, false);
>>>>>>> 45172f9a3310fc6720bdf3d0e0e59d8d1d28e484
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
<<<<<<< HEAD
        // Logic bind data
=======
        Room room = roomList.get(position);

        holder.tvRoomName.setText(room.getName());
        holder.tvCapacity.setText("Sức chứa: " + room.getMaxGuests() + " người");

        NumberFormat currencyFormatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String priceString = currencyFormatter.format(room.getPrice()) + " VNĐ";
        holder.tvPrice.setText(priceString);

        if (room.getFeatures() != null && !room.getFeatures().isEmpty()) {
            StringBuilder features = new StringBuilder();
            for (String f : room.getFeatures()) {
                features.append(f).append(" • ");
            }
            if (features.length() > 3) {
                features.setLength(features.length() - 3);
            }
            holder.tvFeatures.setText(features.toString());
        } else {
            holder.tvFeatures.setText("Tiện ích cơ bản");
        }

        // --- LOGIC ẢNH (Ưu tiên Local -> Mạng) ---
        String localImageName = currentHotel.getId() + "_" + room.getId();
        int resId = context.getResources().getIdentifier(
                localImageName, "drawable", context.getPackageName());

        if (resId != 0) {
            holder.imgRoom.setImageResource(resId);
        } else {
            if (room.getImage() != null && !room.getImage().isEmpty()) {
                // Tự động thêm User-Agent để tránh lỗi 401
                GlideUrl glideUrl = new GlideUrl(room.getImage(), new LazyHeaders.Builder()
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36")
                        .build());

                Glide.with(context)
                        .load(glideUrl)
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image)
                        .into(holder.imgRoom);
            } else {
                holder.imgRoom.setImageResource(R.drawable.placeholder_image);
            }
        }

        // --- SỬA LOGIC NÚT BẤM ---
        holder.btnSelectRoom.setText("Chi tiết"); // Đổi tên nút thành Chi tiết

        holder.btnSelectRoom.setOnClickListener(v -> {
            // Chuyển sang màn hình RoomDetailActivity
            Intent intent = new Intent(context, RoomDetailActivity.class);

            // Truyền cả Room và Hotel sang
            intent.putExtra("room_item", room);
            intent.putExtra("hotel_item", currentHotel);

            context.startActivity(intent);
        });
>>>>>>> 45172f9a3310fc6720bdf3d0e0e59d8d1d28e484
    }

    @Override
    public int getItemCount() {
        return roomList != null ? roomList.size() : 0;
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
<<<<<<< HEAD
        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
=======
        TextView tvRoomName, tvPrice, tvCapacity, tvFeatures;
        ImageView imgRoom;
        Button btnSelectRoom; // Nút bấm

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomName = itemView.findViewById(R.id.tv_room_name);
            tvPrice = itemView.findViewById(R.id.tv_room_price);
            tvCapacity = itemView.findViewById(R.id.tv_room_capacity);
            tvFeatures = itemView.findViewById(R.id.tv_room_features);
            imgRoom = itemView.findViewById(R.id.img_room);

            // Đảm bảo ID này đúng với file item_room_detail.xml
            btnSelectRoom = itemView.findViewById(R.id.btn_select_room);
>>>>>>> 45172f9a3310fc6720bdf3d0e0e59d8d1d28e484
        }
    }
}