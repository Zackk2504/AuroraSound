package org.example.code.repo;

import org.example.code.model.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NhanVienRepository extends JpaRepository<NhanVien, Integer> {
    public Optional<NhanVien> findByTenDangNhap(String tenDangNhap);
    Optional<NhanVien> findByEmail(String email);
}