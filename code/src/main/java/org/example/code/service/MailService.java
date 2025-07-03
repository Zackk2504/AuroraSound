package org.example.code.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    @Autowired
    private JavaMailSender mailSender;

    @Async("threadPoolTaskExecutor")
    public void sendOtpEmail(String email, String otp) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setSubject("Mã xác nhận đăng ký");
        mail.setText("Mã xác nhận của bạn là: " + otp);
        mailSender.send(mail);
    }

    @Async("threadPoolTaskExecutor")
    public void sendWelcomeEmailForNewStaff(String tenNhanVien, String email, String matKhau) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("🎉 Chào mừng bạn đến với hệ thống AuroraSound 🎧");

            String content = "<div style='font-family: Arial, sans-serif; padding: 20px;'>"
                    + "<h2 style='color: #2c3e50;'>Xin chào " + tenNhanVien + "!</h2>"
                    + "<p>🎉 <strong>Chào mừng bạn</strong> đã gia nhập hệ thống quản lý của <strong style='color: #e67e22;'>AuroraSound</strong>.</p>"

                    + "<p>Thông tin tài khoản của bạn:</p>"
                    + "<ul>"
                    + "<li><strong>Email đăng nhập:</strong> " + email + "</li>"
                    + "<li><strong>Mật khẩu:</strong> " + matKhau + "</li>"
                    + "</ul>"

                    + "<p><a href='http://localhost:8080/nhan-vien/login' style='"
                    + "background-color: #3498db; color: white; padding: 10px 20px; text-decoration: none; "
                    + "border-radius: 5px; display: inline-block;'>👉 Đăng nhập ngay</a></p>"

                    + "<p>🔒 Vui lòng đổi mật khẩu sau lần đăng nhập đầu tiên để bảo vệ tài khoản của bạn.</p>"

                    + "<hr style='margin: 30px 0;'>"
                    + "<p style='font-size: 14px;'>Trân trọng,<br><strong>Đội ngũ AuroraSound</strong></p>"

                    + "<div style='margin-top: 30px; text-align: center;'>"
                    + "<img src='https://i.imgur.com/s3hF6LN.png' alt='Welcome Card' style='max-width: 100%; height: auto;'>"
                    + "</div>"
                    + "</div>";

            helper.setText(content, true); // true = HTML

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace(); // hoặc log lỗi nếu cần
        }
    }
}
