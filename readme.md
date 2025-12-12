# 🏨 ỨNG DỤNG ĐẶT PHÒNG KHÁCH SẠN – HOTEL BOOKING APP

---

## 1. Giới thiệu tổng quan

**Hotel Booking App** là ứng dụng di động giúp người dùng tìm kiếm, xem thông tin và đặt phòng khách sạn nhanh chóng, trực quan trên nền tảng Android.
Ứng dụng được thiết kế theo phong cách Material Design, bám sát UI từ Figma – Hotel Booking Mobile App Kit, sử dụng Firebase làm nền tảng backend để đảm bảo khả năng mở rộng, realtime và độ tin cậy cao.

Với Hotel Booking App, người dùng có thể:
Dễ dàng tìm kiếm khách sạn theo vị trí, giá cả, tiện ích.
Đặt phòng và hủy phòng linh hoạt.
Xem bản đồ, định vị khách sạn.
Đánh giá, nhận xét, và xem phản hồi từ người khác.
Nhận thông báo nhắc nhở hoặc khuyến mãi.
Xem vị trí khách sạn trên bản đồ và dẫn đường

---

## 2. Mục tiêu dự án

Mục tiêu của nhóm là xây dựng một ứng dụng đặt phòng khách sạn Hoạt động ổn định trên Android, giao diện thân thiện, giúp người dùng có thể tìm – chọn – đặt phòng trong cùng một nền tảng duy nhất.

Mục tiêu cụ thể:

* **Tìm kiếm & lọc khách sạn:** Cho phép người dùng tìm kiếm nhanh theo tên, địa điểm; có khả năng mở rộng thêm lọc theo giá, đánh giá, tiện ích.

* **Quản lý đặt phòng:** Thực hiện đặt phòng, cập nhật trạng thái, xem lại lịch sử đặt phòng, hỗ trợ hủy phòng linh hoạt.

* **Quản lý thông tin khách sạn:** Hiển thị đầy đủ: tên, mô tả, ảnh, giá, vị trí bản đồ, đánh giá người dùng.

* **Đánh giá & nhận xét:** Cho phép người dùng viết review và xem đánh giá từ khách hàng khác.

* **Thông báo & nhắc nhở:** Cập nhật khuyến mãi, nhắc lịch check-in/check-out, hoặc thay đổi đặt phòng.

* **Tích hợp bản đồ & định vị:** Thể hiện vị trí khách sạn trên bản đồ, hỗ trợ mở Google Maps.

---

## 3. Đối tượng sử dụng

Ứng dụng hướng đến:

* Người dùng có nhu cầu đặt phòng khách sạn, homestay, resort trong nước hoặc quốc tế.
* Độ tuổi từ 18 – 45, sử dụng smartphone Android.
* Người thích du lịch, công tác, phượt, hoặc thuê phòng ngắn hạn.
* Các chủ khách sạn nhỏ lẻ muốn quản lý thông tin phòng, đặt chỗ, đánh giá dễ dàng.

---

## 4. Công nghệ sử dụng

Dự án sử dụng stack công nghệ:
* **Ngôn ngữ**: Java
* **UI Layout**: XML (xây dựng dựa trên templete Figma)
* **Backend**: Firebase
* **Database**: Realtime Database Cloud Firestore và Cloud Storage để lưu trữ hình ảnh.
* Bản đồ & định vị: Google Maps SDK for Android

---

## 5. Các chức năng cốt lõi
### 5.1. Đăng nhập và bảo mật
* Đăng ký tài khoản: Bằng email và mật khẩu.
* Đăng nhập: Xác thực qua Firebase Auth.
* Quản lý phiên đăng nhập: Firebase tự duy trì session, user không phải đăng nhập lại mỗi khi mở app (trừ khi logout).

### 5.2. Tìm kiếm và xem danh sách khách sạn
* Danh sách khách sạn: Dữ liệu lấy từ collection hotels trên Firestore.
* Lọc & tìm kiếm: Theo tên khách sạn, vị trí, giá phòng, tiện ích,...
* Xem chi tiết: Hiển thị mô tả, ảnh, giá, tiện ích.

### 5.3. Đặt và hủy phòng
* Đặt phòng: Người dùng chọn ngày check-in/check-out, số lượng khách và loại phòng.
* Xác nhận đặt: Nhận thông tin đặt phòng và mã giao dịch.
* Hủy phòng: Cho phép hủy trong thời hạn quy định, cập nhật trạng thái booking.
* Lịch sử đặt phòng: Hiển thị danh sách các booking đã thực hiện.

### 5.4. Đánh giá và nhận xét
* Viết đánh giá: Gửi nhận xét và chấm điểm (1–5 sao).
* Xem đánh giá: Tổng hợp các review từ người dùng khác.
* Thống kê đánh giá: Trung bình sao, số lượt đánh giá.

### 5.5. Thông báo và nhắc nhở
* Thông báo đẩy (Push Notification): Nhắc lịch check-in/check-out, hoặc thông tin ưu đãi (nếu có).
* Tùy chọn bật/tắt thông báo: Người dùng chủ động quản lý thông báo. (chưa phát triển xong tính năng này).

### 5.6. Tích hợp bản đồ
* Lưu vị trí khách sạn
* Xem vị trí khách sạn trên bản đồ: Mở Google Map hoặc bản đồ trong app.
* Định vị người dùng và hiển thị khách sạn ở gần dựa trên bán kính tìm kiếm. (Mong muốn mở rộng thêm tính năng này).

---

### 6. Phân quyền người dùng
Trong phạm vi đồ án, hệ thống phân quyền cơ bản:

🧑‍💼 Admin (dữ liệu quản lý qua Firestore Console)
* Quản lý danh sách khách sạn, phòng, đánh giá.
* Duyệt hoặc xóa các đánh giá vi phạm.
* Theo dõi tổng số booking và người dùng.
* Bổ sung / cập nhật dữ liệu demo cho ứng dụng.

👤 User
* Đăng ký, đăng nhập bằng tài khoản Firebase.
* Tìm kiếm, xem chi tiết phòng, đặt phòng, hủy phòng.
* Viết nhận xét, đánh giá sau khi lưu trú.
* Nhận thông báo, quản lý tài khoản cá nhân.

---

### 7. Hình ảnh Demo ứng dụng
* https://drive.google.com/drive/folders/1ZSpbHQCZrGLEnjBijI9pfRz63lcOXwxC?usp=sharing