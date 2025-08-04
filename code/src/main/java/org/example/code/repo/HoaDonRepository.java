package org.example.code.repo;

import org.example.code.model.HoaDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {
    Optional<HoaDon> getHoaDonById(Integer id);

    List<HoaDon> findByTrangThaiHoaDon(String trangThaiHoaDon);

    Optional<HoaDon> findByMaHoaDon(String ma);

    @Query("""
    SELECT h FROM HoaDon h
    WHERE (:sdt IS NULL OR LOWER(h.sdtNguoiMua) LIKE LOWER('%' + :sdt + '%'))
      AND (:trangThai IS NULL OR h.trangThaiHoaDon = :trangThai)
      AND (:loai IS NULL OR h.loaiHoaDon = :loai)
      AND (:hinhThuc IS NULL OR h.hinhThucThanhToan = :hinhThuc)
      AND h.trangThaiHoaDon <> 'CHO_THANH_TOAN'
""")
    Page<HoaDon> findWithFilters(
            @Param("sdt") String sdt,
            @Param("trangThai") String trangThai,
            @Param("loai") String loai,
            @Param("hinhThuc") String hinhThuc,
            Pageable pageable
    );
}