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
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public void LoginWithGG(Authentication authentication) {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");

        Optional<?> existingUser = Optional.ofNullable(accountRepository.findByEmail(email));
        if (existingUser.isEmpty()) {
            // Tạo mới user trong database
            Taikhoan newUser = new Taikhoan();
            newUser.setEmail(email);
            newUser.setUsername(oauthUser.getAttribute("email")); // Sử dụng email làm username
            newUser.setPass(passwordEncoder.encode("oauth2_default_password")); // Mật khẩu mặc định hoặc mã hóa
            newUser.setHoTen(oauthUser.getAttribute("name"));
            // random avatar hoặc dữ liệu mặc định
            accountRepository.save(newUser);
        }
    }
}
