package org.example.code.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
}
