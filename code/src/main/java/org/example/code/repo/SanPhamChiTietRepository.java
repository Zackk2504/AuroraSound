package org.example.code.repo;

import org.example.code.model.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet, Integer> {
    List<SanPhamChiTiet> findAllByTrangThai(String trangThai);
    List<SanPhamChiTiet> findByIdSanpham_Id(Integer sanPhamId);
    @Query("""
    SELECT s FROM SanPhamChiTiet s
    WHERE (:idThuongHieu IS NULL OR s.idSanpham.idThuonghieu.id = :idThuongHieu)
      AND (:idMauSac IS NULL OR s.idMausac.id = :idMauSac)
      AND (:idPhienBan IS NULL OR s.idPhienban.id = :idPhienBan)
      AND (:idXuatXu IS NULL OR s.idSanpham.idXuatxu.id = :idXuatXu)
      AND (:idLoaiSanPham IS NULL OR s.idSanpham.idLoaisanpham.id = :idLoaiSanPham)
      AND s.idSanpham.trangThai = 'hoat_dong'
""")
    List<SanPhamChiTiet> locSanPham(
            @Param("idThuongHieu") Integer idThuongHieu,
            @Param("idMauSac") Integer idMauSac,
            @Param("idPhienBan") Integer idPhienBan,
            @Param("idXuatXu") Integer idXuatXu,
            @Param("idLoaiSanPham") Integer idLoaiSanPham
    );
    Integer countByIdSanpham_Id(Integer idSanPham);

    @Query("SELECT COUNT(s) FROM SanPhamChiTiet s WHERE s.idSanpham.id = :idSanPham")
    Long countBienTheBySanPhamId(@Param("idSanPham") Integer idSanPham);
}