package com.example.hotelbookingapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide; // Thư viện hiển thị ảnh
import com.example.hotelbookingapp.R;
import com.example.hotelbookingapp.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPhone;
    private TextView btnSaveTop, tvChangePhoto;
    private Button btnCancel;
    private ImageView imgAvatar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage; // Khai báo Storage

    private Uri selectedImageUri = null; // Lưu đường dẫn ảnh trên máy

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance(); // Khởi tạo Storage

        // Ánh xạ View
        etName = findViewById(R.id.et_edit_name);
        etEmail = findViewById(R.id.et_edit_email);
        etPhone = findViewById(R.id.et_edit_phone);
        btnSaveTop = findViewById(R.id.btn_save_top);
        btnCancel = findViewById(R.id.btn_cancel);

        // Đảm bảo ID trong XML đúng: img_edit_avatar và tv_change_photo
        imgAvatar = findViewById(R.id.img_edit_avatar);
        tvChangePhoto = findViewById(R.id.tv_change_photo);

        findViewById(R.id.btn_back_edit).setOnClickListener(v -> finish());
        btnCancel.setOnClickListener(v -> finish());

        // --- SỰ KIỆN CHỌN ẢNH ---
        View.OnClickListener pickImageAction = v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            pickImageLauncher.launch(intent);
        };

        if (imgAvatar != null) imgAvatar.setOnClickListener(pickImageAction);
        if (tvChangePhoto != null) tvChangePhoto.setOnClickListener(pickImageAction);

        loadCurrentData();

        // --- SỰ KIỆN LƯU ---
        btnSaveTop.setOnClickListener(v -> {
            if (selectedImageUri != null) {
                // Có chọn ảnh mới -> Upload ảnh trước -> Lấy link -> Lưu data
                uploadImageAndSaveProfile();
            } else {
                // Không đổi ảnh -> Chỉ lưu thông tin chữ
                saveProfile(null);
            }
        });
    }

    // Bộ nhận kết quả chọn ảnh từ thư viện
    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData(); // Lấy đường dẫn file
                    if (imgAvatar != null) {
                        imgAvatar.setImageURI(selectedImageUri); // Hiển thị xem trước
                    }
                }
            }
    );

    // Hàm upload ảnh lên Firebase Storage
    private void uploadImageAndSaveProfile() {
        Toast.makeText(this, "Đang tải ảnh lên...", Toast.LENGTH_SHORT).show();
        String uid = mAuth.getCurrentUser().getUid();

        // Tạo tên file: profile_images/ID_CUA_USER.jpg
        StorageReference ref = storage.getReference().child("profile_images/" + uid);

        ref.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Upload thành công -> Lấy Link download
                    ref.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        // Có link rồi thì lưu vào Firestore
                        saveProfile(downloadUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi upload ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveProfile(String newAvatarUrl) {
        String uid = mAuth.getCurrentUser().getUid();
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Tên không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("fullname", name);
        updates.put("phone", phone);

        // Nếu có link ảnh mới thì update thêm trường này
        if (newAvatarUrl != null) {
            updates.put("avatarUrl", newAvatarUrl);
        }

        db.collection(Constants.KEY_COLLECTION_USERS).document(uid)
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // Đóng màn hình
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi lưu data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void loadCurrentData() {
        if (mAuth.getCurrentUser() == null) return;
        String uid = mAuth.getCurrentUser().getUid();

        db.collection(Constants.KEY_COLLECTION_USERS).document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        etName.setText(documentSnapshot.getString("fullname"));
                        etEmail.setText(documentSnapshot.getString("email"));
                        etPhone.setText(documentSnapshot.getString("phone"));

                        String avatarUrl = documentSnapshot.getString("avatarUrl");
                        if (avatarUrl != null && !avatarUrl.isEmpty() && imgAvatar != null) {
                            Glide.with(this).load(avatarUrl).into(imgAvatar);
                        }
                    }
                });
    }
}