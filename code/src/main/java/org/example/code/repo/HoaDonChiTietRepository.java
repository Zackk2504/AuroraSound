package org.example.code.repo;

import org.example.code.model.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet, Integer> {
    List<HoaDonChiTiet> findAllByIdHoadon_Id(Integer idHoaDon);

    Optional<HoaDonChiTiet> findByIdSanphamchitiet_IdAndIdHoadon_Id(Integer idSanPhamChiTiet, Integer idHoaDon);
}