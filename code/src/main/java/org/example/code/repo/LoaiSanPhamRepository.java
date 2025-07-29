package org.example.code.repo;

import org.example.code.model.LoaiSanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoaiSanPhamRepository extends JpaRepository<LoaiSanPham, Integer> {
//    Page<LoaiSanPham> findAll(Pageable pageable);
}