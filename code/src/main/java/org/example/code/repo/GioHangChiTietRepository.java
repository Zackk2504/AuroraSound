package org.example.code.repo;

import org.example.code.model.GioHangChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GioHangChiTietRepository extends JpaRepository<GioHangChiTiet, Integer> {
    public List<GioHangChiTiet> findByIdGiohang_Id(Integer idGiohang);
    public Optional<GioHangChiTiet> findByIdGiohang_IdAndIdSanphamchitiet_Id(Integer idGiohang, Integer idSanphamchitiet);
}