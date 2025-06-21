package org.example.code.repo;

import org.example.code.model.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
}