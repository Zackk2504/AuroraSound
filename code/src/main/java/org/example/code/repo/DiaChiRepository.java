package org.example.code.repo;

import org.example.code.model.DiaChi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiaChiRepository extends JpaRepository<DiaChi, Integer> {
    List<DiaChi> findByKhachHang_Id(Integer khachHangId);
//    Boolean findByKhachHang_Id()
    Optional<DiaChi> findByMacdinh(boolean macdinh);
}