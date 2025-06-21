package org.example.code.repo;

import org.example.code.model.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NhanVienRepository extends JpaRepository<NhanVien, Integer> {
}