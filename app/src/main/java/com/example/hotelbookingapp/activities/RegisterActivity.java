package com.example.hotelbookingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// QUAN TRỌNG: Import đúng file R
import com.example.hotelbookingapp.R;
import com.example.hotelbookingapp.models.User;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private Button btnCreate;
    private TextView tvLoginLink;
    private ImageView btnBack;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Khởi tạo Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Ánh xạ View
        etName = findViewById(R.id.et_signup_name);
        etEmail = findViewById(R.id.et_signup_email);
        etPassword = findViewById(R.id.et_signup_password);
        btnCreate = findViewById(R.id.btn_signup_create);
        tvLoginLink = findViewById(R.id.tv_login_link);
        btnBack = findViewById(R.id.btn_back);

        // Xử lý sự kiện
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        tvLoginLink.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        btnCreate.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        // Lấy dữ liệu từ ô nhập (đổi tên biến name -> fullname cho rõ ràng)
        String fullname = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(fullname) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Tạo tài khoản trên Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        saveUserToFirestore(firebaseUser, fullname, email, password);
                    } else {
                        Toast.makeText(RegisterActivity.this, "Signup Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserToFirestore(FirebaseUser firebaseUser, String fullname, String email, String password) {
        if (firebaseUser == null) return;

        // String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        // QUAN TRỌNG: Tạo đối tượng User khớp với Constructor 6 tham số bên Model
        User user = new User(
                fullname,       // Truyền đúng fullname vào đây
                email,
                password,       // Lưu ý: Thực tế nên mã hóa hoặc không lưu pass
                "",             // phone (chưa có nên để trống)
                "USER",         // role mặc định
                0               // rewardPoints (int) - để số 0
        );

        // Nếu bạn muốn lưu ngày tạo, hãy bỏ comment trong User.java và dùng user.setCreatedAt(currentDate);

        // 2. Lưu vào Firestore collection "users" với ID trùng với UID
        db.collection("users").document(firebaseUser.getUid())
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RegisterActivity.this, "Account Created!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                    // Xóa các activity cũ để user không back lại được
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RegisterActivity.this, "Error saving data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}