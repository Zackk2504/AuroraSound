package org.example.code.repo;

import org.example.code.model.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KhachHangRepository extends JpaRepository<KhachHang, Integer> {
     Optional<KhachHang> findByEmail(String email);
     Optional<KhachHang> findByTenDangNhap(String tenDangNhap);
     Optional<KhachHang> findBySoDT(String soDT);
//     Optional<KhachHang> findByEmail(String id);
}