package org.example.code.repo;

import org.example.code.model.HoaDon;
import org.example.code.model.LichSuHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LichSuHoaDonRepository extends JpaRepository<LichSuHoaDon, Integer> {

    public List<LichSuHoaDon> findAllByHoaDon_Id(Integer idhd);

}