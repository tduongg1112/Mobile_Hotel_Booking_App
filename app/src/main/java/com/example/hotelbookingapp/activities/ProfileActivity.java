
package com.example.hotelbookingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.hotelbookingapp.R;
import com.example.hotelbookingapp.utils.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvName, tvEmail, tvRole, tvPhone;
    private Button btnLogout, btnEditProfile, btnAdminPanel;
    private ImageView imgAvatar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Kiểm tra login... (giữ nguyên logic cũ của bạn)
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
            return;
        }

        initViews();
        setupEvents();
        loadUserProfile();
        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav == null) return;

        bottomNav.setSelectedItemId(R.id.nav_profile);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            else if (id == R.id.nav_explore) {
                startActivity(new Intent(getApplicationContext(), ExploreActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            else if (id == R.id.nav_my_booking) {
                startActivity(new Intent(getApplicationContext(), MyBookingActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            else if (id == R.id.nav_profile) {
                return true; // Đang ở đây
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserProfile();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav != null) bottomNav.setSelectedItemId(R.id.nav_profile);
    }

    // ... (Các hàm initViews, setupEvents, loadUserProfile giữ nguyên như cũ) ...
    private void initViews() {
        tvName = findViewById(R.id.tv_profile_name);
        tvEmail = findViewById(R.id.tv_profile_email);
        tvRole = findViewById(R.id.tv_profile_role);
        tvPhone = findViewById(R.id.tv_profile_phone);
        imgAvatar = findViewById(R.id.img_profile_avatar);
        btnLogout = findViewById(R.id.btn_logout);
        btnEditProfile = findViewById(R.id.btn_goto_edit);
        btnAdminPanel = findViewById(R.id.btn_admin_panel);
    }

    private void setupEvents() {
        findViewById(R.id.btn_back_profile).setOnClickListener(v -> finish());
        btnEditProfile.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class)));
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(ProfileActivity.this, WelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        btnAdminPanel.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, AdminActivity.class)));
    }

    private void loadUserProfile() {
        if (mAuth.getCurrentUser() == null) return;
        db.collection(Constants.KEY_COLLECTION_USERS).document(mAuth.getCurrentUser().getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        tvName.setText(documentSnapshot.getString("fullname"));
                        tvEmail.setText(documentSnapshot.getString("email"));
                        String role = documentSnapshot.getString("role");
                        tvRole.setText(role);
                        tvPhone.setText(documentSnapshot.getString("phone"));

                        if ("ADMIN".equals(role)) {
                            btnAdminPanel.setVisibility(View.VISIBLE);
                        } else {
                            btnAdminPanel.setVisibility(View.GONE);
                        }

                        String avatarUrl = documentSnapshot.getString("avatarUrl");
                        if (avatarUrl != null && !avatarUrl.isEmpty()) {
                            Glide.with(this).load(avatarUrl).circleCrop().into(imgAvatar);
                        }
                    }
                });
    }
}