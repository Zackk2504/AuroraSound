package org.example.code.repo;

import org.example.code.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    Voucher findByMaVoucher(String maVoucher);
    List<Voucher> findByTrangThai(String trangthai); // Lấy danh sách voucher đang hoạt động
    @Modifying
    @Query("UPDATE Voucher v SET v.trangThai = :status WHERE v.ngayKetThuc < :now AND v.trangThai != 'DA_KET_THUC'")
    int updateStatusForExpiredVouchers(@Param("status") String status, @Param("now") LocalDateTime now);

    @Modifying
    @Query("UPDATE Voucher v SET v.trangThai = :status WHERE v.ngayBatDau <= :now AND v.ngayKetThuc >= :now AND v.trangThai != 'DANG_DIEN_RA'")
    int updateStatusForActiveVouchers(@Param("status") String status, @Param("now") LocalDateTime now);

    @Modifying
    @Query("UPDATE Voucher v SET v.trangThai = :status WHERE v.ngayBatDau > :now AND v.trangThai != 'SAP_DIEN_RA'")
    int updateStatusForUpcomingVouchers(@Param("status") String status, @Param("now") LocalDateTime now);
}