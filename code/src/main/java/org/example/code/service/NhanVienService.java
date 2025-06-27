package org.example.code.service;

import jakarta.annotation.PostConstruct;
import org.example.code.model.NhanVien;
import org.example.code.model.VaiTro;
import org.example.code.repo.NhanVienRepository;
import org.example.code.repo.VaiTroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
}
