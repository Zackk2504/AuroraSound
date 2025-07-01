package org.example.code.service;

import jakarta.servlet.http.HttpSession;
import org.example.code.DTO.DangKiDTO;
import org.example.code.model.GioHang;
import org.example.code.model.KhachHang;


import org.example.code.repo.KhachHangRepository;
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

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    KhachHangRepository accountRepository;

    @Autowired
    private HttpSession session;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MailService mailService;
    @Autowired
    GioHangService gioHangService;

    public void taoVaGuiMaDangKi(DangKiDTO dto) {
//        Kiểm tra xem email đã tồn tại chưa
        Optional<KhachHang> existingUser = accountRepository.findByEmail(dto.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Email đã được sử dụng");
        }
        Optional<KhachHang> khachHangOptional = accountRepository.findByTenDangNhap(dto.getUserName());
        if (khachHangOptional.isPresent()) {
            throw new RuntimeException("Tên đăng nhập đã được sử dụng");
        }

        String otp = String.valueOf((int) (Math.random() * 900000) + 100000);

        // Lưu thông tin vào session
        session.setAttribute("otp", otp);
        session.setAttribute("email", dto.getEmail());
        session.setAttribute("pass", dto.getPass());
        session.setAttribute("username", dto.getUserName());
        System.out.println(dto.getUserName() + " :tao ten la");
        session.setAttribute("otpExpire", System.currentTimeMillis() + 5 * 60 * 1000);
        session.setAttribute("hoTen", dto.getHoTen());
        session.setAttribute("soDT", dto.getSoDT());
//        session.setAttribute("ngaySinh", dto.getNgaySinh());
        session.setAttribute("gioiTinh", dto.getGioiTinh());
        // Gửi mail async
        mailService.sendOtpEmail(dto.getEmail(), otp);
    }

    public boolean XacMinhMa(String maXacNhan) {
        String otp = (String) session.getAttribute("otp");
        String email = (String) session.getAttribute("email");
        String pass = (String) session.getAttribute("pass");
        String username = (String) session.getAttribute("username");
        String hoTen = (String) session.getAttribute("hoTen");
        String soDT = (String) session.getAttribute("soDT");
        String ngaySinh = (String) session.getAttribute("ngaySinh");
        String gioiTinh = (String) session.getAttribute("gioiTinh");
        KhachHang taikhoan;
        if (otp != null && otp.equals(maXacNhan)) {
            if (email == null || pass == null || username == null || hoTen == null || soDT == null || ngaySinh == null || gioiTinh == null) {
                throw new RuntimeException("Thông tin đăng ký không đầy đủ");
            }
            Optional<KhachHang> khachHang = accountRepository.findBySoDT(soDT);
            if (khachHang.isPresent() && khachHang.get().getEmail() == null) {
               taikhoan = khachHang.get();
            }else {
                taikhoan = new KhachHang();
            }
            taikhoan.setEmail(email);
            taikhoan.setMatKhau(passwordEncoder.encode(pass));
            taikhoan.setTenDangNhap(username);
            taikhoan.setHoTen(hoTen);
            taikhoan.setSoDT(soDT);
            taikhoan.setNgaySinh(ngaySinh);
            taikhoan.setGioiTinh(gioiTinh);
            accountRepository.save(taikhoan);
//           Taoj gio hang
            GioHang gioHang = new GioHang();
            LocalDate now = LocalDate.now();
            gioHang.setIdKhachhang(taikhoan);
            gioHang.setNgayTao(now);
            gioHangService.add(gioHang);

            session.removeAttribute("otp");
//            session.removeAttribute("email");
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
            KhachHang newUser = new KhachHang();
            newUser.setEmail(email);
            newUser.setTenDangNhap(oauthUser.getAttribute("email")); // Sử dụng email làm username
            newUser.setMatKhau(passwordEncoder.encode("oauth2_default_password")); // Mật khẩu mặc định
            newUser.setHoTen(oauthUser.getAttribute("name"));
            newUser.setSoDT(oauthUser.getAttribute("phone_number")); // Nếu có
            newUser.setNgaySinh(oauthUser.getAttribute("birthdate")); // Nếu có
            // tạo giỏ hàng
            GioHang gioHang = new GioHang();
            LocalDate now = LocalDate.now();
            gioHang.setIdKhachhang(newUser);
            gioHang.setNgayTao(now);
            gioHangService.add(gioHang);
            accountRepository.save(newUser);
        }
    }
}
