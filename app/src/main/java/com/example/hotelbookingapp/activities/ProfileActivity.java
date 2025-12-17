package com.example.hotelbookingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView; // Import ImageView
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// Thư viện Glide
import com.bumptech.glide.Glide;

import com.example.hotelbookingapp.R;
import com.example.hotelbookingapp.utils.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvName, tvEmail, tvRole, tvPhone;
    private Button btnLogout, btnEditProfile;
    private ImageView imgAvatar; // Ảnh đại diện

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Kiểm tra đăng nhập
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
            return;
        }

        // Ánh xạ View
        tvName = findViewById(R.id.tv_profile_name);
        tvEmail = findViewById(R.id.tv_profile_email);
        tvRole = findViewById(R.id.tv_profile_role);
        tvPhone = findViewById(R.id.tv_profile_phone);

        // LƯU Ý: Cần đặt ID cho ImageView trong activity_profile.xml là @+id/img_profile_avatar
        imgAvatar = findViewById(R.id.img_profile_avatar);

        btnLogout = findViewById(R.id.btn_logout);
        btnEditProfile = findViewById(R.id.btn_goto_edit);

        findViewById(R.id.btn_back_profile).setOnClickListener(v -> finish());

        // Chuyển sang trang Edit
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        // Đăng xuất
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(ProfileActivity.this, WelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Cài đặt thanh Navbar dưới cùng
        setupBottomNavigation();

        // Load dữ liệu lần đầu
        loadUserProfile();
    }

    // Quan trọng: Hàm này chạy mỗi khi quay lại màn hình này (ví dụ từ trang Edit về)
    // Giúp cập nhật dữ liệu mới ngay lập tức
    @Override
    protected void onResume() {
        super.onResume();
        loadUserProfile();
    }

    private void loadUserProfile() {
        if (mAuth.getCurrentUser() == null) return;
        String uid = mAuth.getCurrentUser().getUid();

        db.collection(Constants.KEY_COLLECTION_USERS).document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Lấy dữ liệu an toàn
                        String name = documentSnapshot.getString("fullname");
                        String email = documentSnapshot.getString("email");
                        String role = documentSnapshot.getString("role");
                        String phone = documentSnapshot.getString("phone");
                        String avatarUrl = documentSnapshot.getString("avatarUrl"); // Lấy link ảnh

                        // Hiển thị text
                        tvName.setText(name != null ? name : "No Name");
                        tvEmail.setText(email != null ? email : "");
                        tvRole.setText(role != null ? role : "GUEST");
                        tvPhone.setText(phone != null && !phone.isEmpty() ? phone : "--");

                        // Hiển thị ảnh bằng Glide
                        if (avatarUrl != null && !avatarUrl.isEmpty() && imgAvatar != null) {
                            Glide.with(this)
                                    .load(avatarUrl)
                                    .circleCrop() // <--- THÊM DÒNG NÀY VÀO ĐÂY
                                    .placeholder(R.mipmap.ic_launcher_round) // Ảnh chờ khi đang tải
                                    .error(R.mipmap.ic_launcher_round) // Ảnh hiển thị nếu lỗi
                                    .into(imgAvatar);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void setupBottomNavigation() {
        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        nav.setSelectedItemId(R.id.nav_profile);

        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_profile) return true;
            return false;
        });
    }
}