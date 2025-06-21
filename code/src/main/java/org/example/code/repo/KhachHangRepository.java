package org.example.code.repo;

import org.example.code.model.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KhachHangRepository extends JpaRepository<KhachHang, Integer> {
}