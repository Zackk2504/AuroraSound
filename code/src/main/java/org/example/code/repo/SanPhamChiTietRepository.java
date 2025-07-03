package org.example.code.repo;

import org.example.code.model.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet, Integer> {
    List<SanPhamChiTiet> findAllByTrangThai(String trangThai);
    List<SanPhamChiTiet> findByIdSanpham_Id(Integer sanPhamId);
}