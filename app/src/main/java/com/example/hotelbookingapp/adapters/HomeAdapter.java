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
<<<<<<< HEAD
=======
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
>>>>>>> 45172f9a3310fc6720bdf3d0e0e59d8d1d28e484
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

<<<<<<< HEAD
    // --- HÀM MỚI QUAN TRỌNG ĐỂ SEARCH HOẠT ĐỘNG ---
    public void setHotelList(List<Hotel> newList) {
        this.hotelList = newList;
        notifyDataSetChanged(); // Làm mới danh sách hiển thị
    }
    // -----------------------------------------------
=======
    public void setHotelList(List<Hotel> newList) {
        this.hotelList = newList;
        notifyDataSetChanged();
    }
>>>>>>> 45172f9a3310fc6720bdf3d0e0e59d8d1d28e484

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
<<<<<<< HEAD
        // Đảm bảo bạn đã có file item_hotel.xml (xem mục số 2 bên dưới)
=======
>>>>>>> 45172f9a3310fc6720bdf3d0e0e59d8d1d28e484
        View view = LayoutInflater.from(context).inflate(R.layout.item_hotel, parent, false);
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        Hotel hotel = hotelList.get(position);

<<<<<<< HEAD
        // 1. Set Tên
        holder.tvName.setText(hotel.getName());

        // 2. Set Địa điểm
        holder.tvLocation.setText(hotel.getLocation());

        // 3. Set Giá tiền (Format đẹp)
=======
        holder.tvName.setText(hotel.getName());
        holder.tvLocation.setText(hotel.getLocation());
        holder.tvRating.setText(String.valueOf(hotel.getRatingAverage()));

>>>>>>> 45172f9a3310fc6720bdf3d0e0e59d8d1d28e484
        NumberFormat currencyFormatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String priceString = currencyFormatter.format(hotel.getMinPrice()) + " VNĐ";
        holder.tvPrice.setText(priceString);

<<<<<<< HEAD
        // 4. Set Rating
        holder.tvRating.setText(String.valueOf(hotel.getRatingAverage()));

        // 5. Load Ảnh
        if (hotel.getImages() != null && !hotel.getImages().isEmpty()) {
            Glide.with(context)
                    .load(hotel.getImages().get(0))
                    .placeholder(R.drawable.placeholder_image) // Nhớ tạo ảnh này trong drawable
                    .error(R.drawable.error_image)
                    .into(holder.imgThumbnail);
        } else {
            holder.imgThumbnail.setImageResource(R.drawable.placeholder_image);
        }

        // 6. Click sự kiện
=======
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

>>>>>>> 45172f9a3310fc6720bdf3d0e0e59d8d1d28e484
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

<<<<<<< HEAD
    // ViewHolder
=======
>>>>>>> 45172f9a3310fc6720bdf3d0e0e59d8d1d28e484
    public static class HotelViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvLocation, tvPrice, tvRating;
        ImageView imgThumbnail;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
<<<<<<< HEAD
            // Các ID này phải khớp với file item_hotel.xml
=======
>>>>>>> 45172f9a3310fc6720bdf3d0e0e59d8d1d28e484
            tvName = itemView.findViewById(R.id.tv_hotel_name);
            tvLocation = itemView.findViewById(R.id.tv_hotel_location);
            tvPrice = itemView.findViewById(R.id.tv_hotel_price);
            tvRating = itemView.findViewById(R.id.tv_hotel_rating);
            imgThumbnail = itemView.findViewById(R.id.img_hotel_thumbnail);
        }
    }
}