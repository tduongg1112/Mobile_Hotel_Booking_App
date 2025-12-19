package com.example.hotelbookingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.hotelbookingapp.R;
import com.example.hotelbookingapp.activities.DetailActivity;
import com.example.hotelbookingapp.models.Hotel;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HotelViewHolder> {

    private Context context;
    private List<Hotel> hotelList;

    public HomeAdapter(Context context, List<Hotel> hotelList) {
        this.context = context;
        this.hotelList = hotelList;
    }

    public void setHotelList(List<Hotel> newList) {
        this.hotelList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hotel, parent, false);
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        Hotel hotel = hotelList.get(position);

        holder.tvName.setText(hotel.getName());
        holder.tvLocation.setText(hotel.getLocation());
        holder.tvRating.setText(String.format(Locale.US, "%.1f", hotel.getRatingAverage()));

        NumberFormat currencyFormatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String priceString = currencyFormatter.format(hotel.getMinPrice()) + " VNĐ";
        holder.tvPrice.setText(priceString);

        // --- LOGIC ẢNH THÔNG MINH (Ưu tiên Local -> Mạng) ---

        // Bước 1: Tìm xem có ảnh trong drawable trùng tên ID hotel không?
        // Ví dụ: hotel.getId()="hotel_022" -> tìm file hotel_022 trong drawable
        int resId = context.getResources().getIdentifier(
                hotel.getId(), "drawable", context.getPackageName());

        if (resId != 0) {
            // TRƯỜNG HỢP 1: Có ảnh trong máy (hotel_022, hotel_004...) -> Hiện luôn
            holder.imgThumbnail.setImageResource(resId);
        } else {
            // TRƯỜNG HỢP 2: Không có ảnh máy -> Tải từ URL (cho các hotel còn lại)
            String urlToLoad = null;
            if (hotel.getImageUrl() != null && !hotel.getImageUrl().isEmpty()) {
                urlToLoad = hotel.getImageUrl();
            } else if (hotel.getImages() != null && !hotel.getImages().isEmpty()) {
                urlToLoad = hotel.getImages().get(0);
            }

            if (urlToLoad != null) {
                // Giữ nguyên logic User-Agent để sau này bạn fix data sang Unsplash vẫn chạy tốt
                GlideUrl glideUrl = new GlideUrl(urlToLoad, new LazyHeaders.Builder()
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36")
                        .build());

                Glide.with(context)
                        .load(glideUrl)
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image)
                        .into(holder.imgThumbnail);
            } else {
                holder.imgThumbnail.setImageResource(R.drawable.placeholder_image);
            }
        }
        // -----------------------------------------------------

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("hotel_object", hotel);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return hotelList != null ? hotelList.size() : 0;
    }

    public static class HotelViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvLocation, tvPrice, tvRating;
        ImageView imgThumbnail;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_hotel_name);
            tvLocation = itemView.findViewById(R.id.tv_hotel_location);
            tvPrice = itemView.findViewById(R.id.tv_hotel_price);
            tvRating = itemView.findViewById(R.id.tv_hotel_rating);
            imgThumbnail = itemView.findViewById(R.id.img_hotel_thumbnail);
        }
    }
}