package org.example.code.repo;

import org.example.code.model.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {
    Optional<HoaDon> getHoaDonById(Integer id);

    List<HoaDon> findByTrangThaiHoaDon(String trangThaiHoaDon);
}