package com.example.hotelbookingapp.utils;

import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.hotelbookingapp.R;

public class GlideUtils {

    // Hàm load ảnh thông minh: Tự động thêm User-Agent để tránh lỗi 401 của Booking.com
    public static void loadHotelImage(Context context, String url, ImageView imageView) {
        if (url == null || url.isEmpty()) {
            imageView.setImageResource(R.drawable.placeholder_image);
            return;
        }

        try {
            // Tạo Header giả lập trình duyệt Chrome
            GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36")
                    .build());

            Glide.with(context)
                    .load(glideUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
            imageView.setImageResource(R.drawable.error_image);
        }
    }
}