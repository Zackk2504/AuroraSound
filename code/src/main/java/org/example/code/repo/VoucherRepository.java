package org.example.code.repo;

import org.example.code.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    Voucher findByMaVoucher(String maVoucher);
    List<Voucher> findByTrangThai(String trangthai); // Lấy danh sách voucher đang hoạt động
}