package org.example.code.repo;

import org.example.code.model.AnhSanPham;
import org.example.code.model.DiaChi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnhSanPhamRepository extends JpaRepository<AnhSanPham, Integer> {
    List<AnhSanPham> findByIdSanpham_IdOrderByIdDesc(Integer idSanPham);
}