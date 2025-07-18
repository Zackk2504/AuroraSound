package org.example.code.repo;

import org.example.code.model.GioHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GioHangRepository extends JpaRepository<GioHang, Integer> {
    GioHang findByIdKhachhang_Id(Integer khachHangId);
    @Query("SELECT COUNT(DISTINCT ct.idSanphamchitiet.id) FROM GioHangChiTiet ct WHERE ct.idGiohang.idKhachhang.tenDangNhap = :tenDangNhap")
    Long demSoSanPhamTheoTenDangNhap( String tenDangNhap);
}