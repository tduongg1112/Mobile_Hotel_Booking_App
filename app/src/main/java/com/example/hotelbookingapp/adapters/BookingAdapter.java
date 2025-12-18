package com.example.hotelbookingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.hotelbookingapp.R;
import com.example.hotelbookingapp.activities.ReviewActivity;
import com.example.hotelbookingapp.models.Booking;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private Context context;
    private List<Booking> bookingList;

    // Interface xử lý sự kiện Hủy
    private IBookingHandler mInterface;

    public interface IBookingHandler {
        void onCancelClick(Booking booking);
    }

    public BookingAdapter(Context context, List<Booking> bookingList, IBookingHandler mInterface) {
        this.context = context;
        this.bookingList = bookingList;
        this.mInterface = mInterface;
    }

    // Constructor phụ nếu chỉ truyền 2 tham số (để tránh lỗi code cũ nếu có)
    public BookingAdapter(Context context, List<Booking> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        if (booking == null) return;

        // 1. Hiển thị thông tin cơ bản
        holder.tvName.setText(booking.getHotelName());
        holder.tvAddress.setText(booking.getHotelAddress());

        // --- SỬA LỖI Ở ĐÂY: KHÔNG DÙNG SimpleDateFormat NỮA ---
        // Vì dữ liệu của bạn là String nên chỉ cần cộng chuỗi
        String dateStr = booking.getCheckInDate() + " - " + booking.getCheckOutDate();
        holder.tvDate.setText(dateStr);
        // -----------------------------------------------------

        // Format tiền tệ
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.tvPrice.setText(currencyFormatter.format(booking.getTotalPrice()));

        // Hiển thị trạng thái
        holder.tvStatus.setText(booking.getStatus());
        if ("CONFIRMED".equalsIgnoreCase(booking.getStatus()) || "Đã xác nhận".equals(booking.getStatus())) {
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50")); // Xanh lá
            holder.tvStatus.setText("Đã xác nhận");
        } else if ("CANCELLED".equalsIgnoreCase(booking.getStatus()) || "Đã hủy".equals(booking.getStatus())) {
            holder.tvStatus.setTextColor(Color.RED);
            holder.tvStatus.setText("Đã hủy");
        } else {
            holder.tvStatus.setTextColor(Color.parseColor("#FF9800")); // Cam
            holder.tvStatus.setText("Đang chờ");
        }

        // Logic to load hotel image
        int resId = context.getResources().getIdentifier(
                booking.getHotelId(), "drawable", context.getPackageName());

        if (resId != 0) {
            holder.imgCard.setImageResource(resId);
        } else {
            String urlToLoad = booking.getHotelImage();
            if (urlToLoad != null && !urlToLoad.isEmpty()) {
                GlideUrl glideUrl = new GlideUrl(urlToLoad, new LazyHeaders.Builder()
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36")
                        .build());

                Glide.with(context)
                        .load(glideUrl)
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image)
                        .into(holder.imgCard);
            } else {
                holder.imgCard.setImageResource(R.drawable.placeholder_image);
            }
        }

        // --- 2. LOGIC ẨN HIỆN NÚT ---
        String status = booking.getStatus();

        // Kiểm tra lỏng lẻo hơn (bao gồm cả chữ thường/hoa)
        if ("CONFIRMED".equalsIgnoreCase(status) || "COMPLETED".equalsIgnoreCase(status)) {
            // Đơn thành công -> Hiện Đánh giá
            holder.btnRate.setVisibility(View.VISIBLE);
            holder.btnCancel.setVisibility(View.GONE);
        } else if ("CANCELLED".equalsIgnoreCase(status)) {
            // Đã hủy -> Ẩn hết
            holder.btnRate.setVisibility(View.GONE);
            holder.btnCancel.setVisibility(View.GONE);
        } else {
            // Đang chờ -> Hiện Hủy
            holder.btnRate.setVisibility(View.GONE);
            holder.btnCancel.setVisibility(View.VISIBLE);
        }

        // --- 3. SỰ KIỆN NÚT ---
        holder.btnRate.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReviewActivity.class);
            intent.putExtra("HOTEL_ID", booking.getHotelId());
            context.startActivity(intent);
        });

        holder.btnCancel.setOnClickListener(v -> {
            if (mInterface != null) {
                mInterface.onCancelClick(booking);
            } else {
                Toast.makeText(context, "Chức năng hủy chưa được gắn kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingList != null ? bookingList.size() : 0;
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvStatus, tvAddress, tvDate, tvPrice;
        TextView btnCancel, btnRate;
        ImageView imgCard;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            imgCard = itemView.findViewById(R.id.imgCard);
            btnCancel = itemView.findViewById(R.id.btnCancel);
            btnRate = itemView.findViewById(R.id.btnRate);
        }
    }
}