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

    // --- HÀM MỚI QUAN TRỌNG ĐỂ SEARCH HOẠT ĐỘNG ---
    public void setHotelList(List<Hotel> newList) {
        this.hotelList = newList;
        notifyDataSetChanged(); // Làm mới danh sách hiển thị
    }
    // -----------------------------------------------

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Đảm bảo bạn đã có file item_hotel.xml (xem mục số 2 bên dưới)
        View view = LayoutInflater.from(context).inflate(R.layout.item_hotel, parent, false);
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        Hotel hotel = hotelList.get(position);

        // 1. Set Tên
        holder.tvName.setText(hotel.getName());

        // 2. Set Địa điểm
        holder.tvLocation.setText(hotel.getLocation());

        // 3. Set Giá tiền (Format đẹp)
        NumberFormat currencyFormatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String priceString = currencyFormatter.format(hotel.getMinPrice()) + " VNĐ";
        holder.tvPrice.setText(priceString);

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

    // ViewHolder
    public static class HotelViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvLocation, tvPrice, tvRating;
        ImageView imgThumbnail;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            // Các ID này phải khớp với file item_hotel.xml
            tvName = itemView.findViewById(R.id.tv_hotel_name);
            tvLocation = itemView.findViewById(R.id.tv_hotel_location);
            tvPrice = itemView.findViewById(R.id.tv_hotel_price);
            tvRating = itemView.findViewById(R.id.tv_hotel_rating);
            imgThumbnail = itemView.findViewById(R.id.img_hotel_thumbnail);
        }
    }
}