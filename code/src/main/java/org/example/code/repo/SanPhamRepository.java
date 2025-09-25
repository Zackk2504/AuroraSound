package org.example.code.repo;

import org.example.code.model.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
    @Query("SELECT sp FROM SanPham sp " +
            "WHERE (:loaiId IS NULL OR sp.idLoaisanpham.id = :loaiId) " +
            "AND (:thuongHieuId IS NULL OR sp.idThuonghieu.id = :thuongHieuId) " +
            "AND (:xuatXuId IS NULL OR sp.idXuatxu.id = :xuatXuId)")
    List<SanPham> locSanPham(@Param("loaiId") Integer loaiId,
                             @Param("thuongHieuId") Integer thuongHieuId,
                             @Param("xuatXuId") Integer xuatXuId);
    @Query("""
    SELECT sp FROM SanPham sp
    WHERE (:tensp IS NULL OR LOWER(sp.tenSanPham) LIKE LOWER('%' + :tensp + '%'))
      AND (:loaiId IS NULL OR sp.idLoaisanpham.id = :loaiId)
      AND (:thuongHieuId IS NULL OR sp.idThuonghieu.id = :thuongHieuId)
      AND (:xuatXuId IS NULL OR sp.idXuatxu.id = :xuatXuId)
      AND (:trangThai IS NULL OR sp.trangThai = :trangThai)
""")
    Page<SanPham> search(
            @Param("tensp") String tensp,
            @Param("loaiId") Integer loaiId,
            @Param("thuongHieuId") Integer thuongHieuId,
            @Param("xuatXuId") Integer xuatXuId,
            @Param("trangThai") String trangThai,
            Pageable pageable
    );

    List<SanPham> findAllByTrangThai(String trangThai);


    @Query("SELECT sp FROM SanPham sp WHERE LOWER(sp.tenSanPham) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<SanPham> searchByTen(@Param("keyword") String keyword);

}