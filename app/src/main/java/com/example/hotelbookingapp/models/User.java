package com.example.hotelbookingapp.models;

public class User {
    // 1. Sửa fullName -> fullname (cho giống hệt Firestore)
    private String fullname;
    private String email;
    private String password;
    private String phone;
    private String role;

    // 2. rewardPoints giữ là int (số) như bạn yêu cầu
    private int rewardPoints;

    // Nếu trong Firestore bạn có trường createdAt thì thêm vào, nếu không thì bỏ qua
    // private String createdAt;

    public User() { } // Constructor rỗng bắt buộc

    public User(String fullname, String email, String password, String phone, String role, int rewardPoints) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.rewardPoints = rewardPoints;
    }

    // Getter & Setter (Lưu ý tên hàm get đã thay đổi)
    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public int getRewardPoints() { return rewardPoints; }
    public void setRewardPoints(int rewardPoints) { this.rewardPoints = rewardPoints; }
}