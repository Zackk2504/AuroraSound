package org.example.code.repo;

import org.example.code.model.GioHang;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GioHangRepository extends JpaRepository<GioHang, Integer> {
    GioHang findByKhachHangId(Integer khachHangId);
}