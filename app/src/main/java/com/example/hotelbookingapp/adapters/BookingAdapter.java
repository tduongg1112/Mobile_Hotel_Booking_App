package com.example.hotelbookingapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelbookingapp.R;
import com.example.hotelbookingapp.models.Booking;
import com.bumptech.glide.Glide; // Cần thư viện Glide để load ảnh

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<Booking> mListBooking;
    private IBookingHandler mInterface; // Interface để gọi ngược về Activity
    private Context mContext;

    // Interface để xử lý sự kiện bấm nút Hủy
    public interface IBookingHandler {
        void onCancelClick(Booking booking);
    }

    public BookingAdapter(List<Booking> mListBooking, IBookingHandler mInterface) {
        this.mListBooking = mListBooking;
        this.mInterface = mInterface;
    }

    public void setList(List<Booking> list) {
        this.mListBooking = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        // Lưu ý: Bạn cần tạo file layout item_booking.xml (tôi sẽ cung cấp bên dưới)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = mListBooking.get(position);
        if (booking == null) return;

        holder.tvHotelName.setText(booking.getHotelName());
        holder.tvAddress.setText(booking.getHotelAddress());
        holder.tvDate.setText(booking.getCheckInDate() + " - " + booking.getCheckOutDate());

        // Format tiền
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.tvPrice.setText(formatter.format(booking.getTotalPrice()));

        // Xử lý trạng thái và nút Hủy
        if ("cancelled".equals(booking.getStatus())) {
            holder.tvStatus.setText("Đã hủy");
            holder.tvStatus.setTextColor(Color.RED);
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_cancelled); // Tạo bg màu đỏ nhạt nếu cần
            holder.btnCancel.setVisibility(View.GONE); // Đã hủy rồi thì ẩn nút hủy
        } else {
            holder.tvStatus.setText("Đã xác nhận");
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50")); // Xanh lá
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_confirmed); // Tạo bg xanh nhạt nếu cần
            holder.btnCancel.setVisibility(View.VISIBLE); // Hiện nút hủy
        }

        // Load ảnh (Dùng Glide hoặc set ảnh demo)
        // Glide.with(mContext).load(booking.getHotelImage()).into(holder.imgHotel);
        holder.imgHotel.setImageResource(R.drawable.img_map_background); // Ảnh demo tạm thời

        // Sự kiện bấm nút Hủy
        holder.btnCancel.setOnClickListener(v -> mInterface.onCancelClick(booking));
    }

    @Override
    public int getItemCount() {
        return mListBooking != null ? mListBooking.size() : 0;
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHotel;
        TextView tvHotelName, tvAddress, tvDate, tvStatus, tvPrice, btnCancel;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ ID theo file item_booking.xml
            imgHotel = itemView.findViewById(R.id.imgCard);
            tvHotelName = itemView.findViewById(R.id.tvName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnCancel = itemView.findViewById(R.id.btnCancel);
        }
    }
}