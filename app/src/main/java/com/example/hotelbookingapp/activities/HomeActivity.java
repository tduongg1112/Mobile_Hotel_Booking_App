package com.example.hotelbookingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.hotelbookingapp.R;
import com.example.hotelbookingapp.fragments.ExploreFragment;
import com.example.hotelbookingapp.fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNav = findViewById(R.id.bottom_navigation);

        // Mặc định load HomeFragment (Giao diện màu cam của bạn)
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
            bottomNav.setSelectedItemId(R.id.nav_home);
        }

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                // Load lại Fragment Home cũ của bạn
                loadFragment(new HomeFragment());
                return true;
            }
            else if (id == R.id.nav_explore) {
                // Load lại Fragment Explore cũ của bạn
                loadFragment(new ExploreFragment());
                return true;
            }
            else if (id == R.id.nav_my_booking) {
                // Chuyển sang màn hình MyBookingActivity
                Intent intent = new Intent(HomeActivity.this, MyBookingActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return false; // Trả về false để không đổi màu tab ở đây (vì đã chuyển Activity)
            }
            else if (id == R.id.nav_profile) {
                // Chuyển sang màn hình ProfileActivity
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return false;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    // Giữ trạng thái icon khi quay lại từ Booking/Profile
    @Override
    protected void onResume() {
        super.onResume();
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof HomeFragment) {
            bottomNav.setSelectedItemId(R.id.nav_home);
        } else if (currentFragment instanceof ExploreFragment) {
            bottomNav.setSelectedItemId(R.id.nav_explore);
        }
    }
}