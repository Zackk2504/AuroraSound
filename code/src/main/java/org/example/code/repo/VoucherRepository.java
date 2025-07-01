package org.example.code.repo;

import org.example.code.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    Voucher findByMaVoucher(String maVoucher);
}