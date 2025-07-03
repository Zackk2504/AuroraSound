package org.example.code.service;

import jakarta.annotation.PostConstruct;
import org.example.code.model.NhanVien;
import org.example.code.model.VaiTro;
import org.example.code.repo.NhanVienRepository;
import org.example.code.repo.VaiTroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
@DependsOn("vaiTroService")
public class NhanVienService {
    @Autowired
    VaiTroRepository vaiTroRepository;

    @Autowired
    NhanVienRepository nhanVienRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MailService mailService;
    @Autowired
    VaiTroService vaiTroService;

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*";
    private static final String ALL = UPPER + LOWER + DIGITS + SPECIAL;
    private static final SecureRandom random = new SecureRandom();

    @PostConstruct
    public void initAdminAccount() {
        String defaultUsername = "admin";

        if (nhanVienRepository.findByTenDangNhap(defaultUsername).isEmpty()) {
            Optional<VaiTro> vaiTroAdmin = vaiTroRepository.findByTenVaiTro("admin");
            if (vaiTroAdmin.isEmpty()) {
                VaiTro vaiTro = new VaiTro();
                vaiTro.setTenVaiTro("admin");

                vaiTroRepository.save(vaiTro);
            }

            // Tạo tài khoản admin mặc định
            VaiTro vaiTro = vaiTroAdmin.get();
            NhanVien admin = new NhanVien();
            admin.setTenDangNhap("admin");
            admin.setMatKhau(passwordEncoder.encode("admin"));
            admin.setVaiTro(vaiTro);
            admin.setHoTen("Admin");
            admin.setEmail("admin");
            admin.setTrangThai(true);

            nhanVienRepository.save(admin);
            System.out.println("✅ Đã tạo tài khoản admin mặc định.");
        }
    }

    public NhanVien getNhanVienById(Integer id) {
        return nhanVienRepository.findById(id).orElse(null);
    }
    public NhanVien getNhanVienByTenDangNhap(String tenDangNhap) {
        return nhanVienRepository.findByTenDangNhap(tenDangNhap).orElse(null);
    }
    public List<NhanVien> getAll() {
        return nhanVienRepository.findAll();
    }



        public static String generateRandomPassword(int length) {
            StringBuilder password = new StringBuilder();

            // Đảm bảo có ít nhất 1 ký tự của mỗi loại
            password.append(UPPER.charAt(random.nextInt(UPPER.length())));
            password.append(LOWER.charAt(random.nextInt(LOWER.length())));
            password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
            password.append(SPECIAL.charAt(random.nextInt(SPECIAL.length())));

            // Các ký tự còn lại
            for (int i = 4; i < length; i++) {
                password.append(ALL.charAt(random.nextInt(ALL.length())));
            }

            return password.toString();
        }

    public void themNhanVien(NhanVien nhanVien) {
        VaiTro vaiTro = vaiTroService.getVaiTroByName("employee");
        Optional<NhanVien> existingNhanVien = nhanVienRepository.findByEmail(nhanVien.getEmail());
        if (existingNhanVien.isPresent()) {
            throw new RuntimeException("Email đã được sử dụng bởi nhân viên khác.");
        }
        String pass = generateRandomPassword(8);
        mailService.sendWelcomeEmailForNewStaff(nhanVien.getHoTen(), nhanVien.getEmail(), pass);
        passwordEncoder.encode(pass);
        nhanVien.setMatKhau(pass);
        nhanVien.setTenDangNhap(nhanVien.getEmail());
        nhanVien.setVaiTro(vaiTro);
        nhanVien.setTrangThai(true);
        nhanVienRepository.save(nhanVien);
    }

    public Page<NhanVien> getAll(Pageable pageable) {
        return nhanVienRepository.findAll(pageable);
    }
    public NhanVien addAndUpdate(NhanVien nhanVien){
        return nhanVienRepository.save(nhanVien);
    }
    public Optional<NhanVien> getById(Integer id) {
        return nhanVienRepository.findById(id)
    ;}
}
