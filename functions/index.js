const {onSchedule} = require("firebase-functions/v2/scheduler");
const {logger} = require("firebase-functions");
const {initializeApp} = require("firebase-admin/app");
const {getFirestore} = require("firebase-admin/firestore");

initializeApp();
const db = getFirestore();

/**
 * Hàm tự động chạy vào 9:00 sáng mỗi ngày (Giờ VN).
 */
exports.sendCheckInReminders = onSchedule(
    {
      schedule: "every day 09:00",
      timeZone: "Asia/Ho_Chi_Minh",
    },
    async (event) => {
      logger.info("Bắt đầu chạy hàm gửi thông báo nhắc check-in...");

      try {
        const tomorrow = new Date();
        tomorrow.setDate(tomorrow.getDate() + 1);

        const day = String(tomorrow.getDate()).padStart(2, "0");
        const month = String(tomorrow.getMonth() + 1).padStart(2, "0");
        const year = tomorrow.getFullYear();
        const tomorrowDateString = `${day}/${month}/${year}`;

        logger.info(`Tìm booking check-in ngày: ${tomorrowDateString}`);

        const querySnapshot = await db.collection("bookings")
            .where("status", "==", "CONFIRMED")
            .where("checkInDate", "==", tomorrowDateString)
            .get();

        if (querySnapshot.empty) {
          logger.info("Không có booking nào ngày mai.");
          return null;
        }

        logger.info(`Tìm thấy ${querySnapshot.size} booking.`);

        const notificationPromises = [];

        querySnapshot.forEach((doc) => {
          const booking = doc.data();
          const {userId, hotelName} = booking;

          const notificationPayload = {
            userId: userId,
            title: "Nhắc nhở nhận phòng",
            body: `Bạn có lịch tại ${hotelName} vào ngày mai.`,
            timestamp: new Date(),
            isRead: false,
          };

          const promise = db.collection("notifications")
              .add(notificationPayload);
          notificationPromises.push(promise);
        });

        await Promise.all(notificationPromises);
        logger.info(`Đã tạo ${notificationPromises.length} thông báo.`);
      } catch (error) {
        logger.error("Lỗi khi gửi thông báo:", error);
      }

      return null;
    });