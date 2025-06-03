package org.example.code.service;

import jakarta.servlet.http.HttpSession;
import org.example.code.DTO.DangKiDTO;
import org.example.code.model.Taikhoan;
import org.example.code.repo.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private HttpSession session;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MailService mailService;

    public Taikhoan add(Taikhoan taikhoan) {
        taikhoan.setRole("1");
        taikhoan.setHoTen("123");
        taikhoan.setTrangThai("123");
        return accountRepository.save(taikhoan);
    }
    public void taoVaGuiMaDangKi(DangKiDTO dto) {
        String otp = String.valueOf((int) (Math.random() * 900000) + 100000);

        // Lưu thông tin vào session
        session.setAttribute("otp", otp);
        session.setAttribute("email", dto.getEmail());
        session.setAttribute("pass", dto.getPass());
        session.setAttribute("username", dto.getUserName());
        System.out.println(dto.getUserName() + " :tao ten la");
        session.setAttribute("otpExpire", System.currentTimeMillis() + 5 * 60 * 1000);

        // Gửi mail async
        mailService.sendOtpEmail(dto.getEmail(), otp);
    }

    public boolean XacMinhMa(String maXacNhan) {
        String otp = (String) session.getAttribute("otp");
        String email = (String) session.getAttribute("email");
        String pass = (String) session.getAttribute("pass");
        String username = (String) session.getAttribute("username");

        if (otp != null && otp.equals(maXacNhan)) {
            Taikhoan taikhoan = new Taikhoan();
            taikhoan.setEmail(email);
            taikhoan.setPass(passwordEncoder.encode(pass));
            taikhoan.setUsername(username);
            accountRepository.save(taikhoan);

            session.removeAttribute("otp");
            session.removeAttribute("email");
            session.removeAttribute("pass");
            session.removeAttribute("username");
            session.removeAttribute("otpExpire");

            return true;
        }
        return false;
    }
}
