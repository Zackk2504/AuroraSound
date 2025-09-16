package org.example.code.repo;

import org.example.code.model.ThuongHieu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThuongHieuRepository extends JpaRepository<ThuongHieu, Integer> {
    List<ThuongHieu> findAllByTrangThai(boolean trangThai);
}