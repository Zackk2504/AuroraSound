package org.example.code.service;

import org.example.code.model.Voucher;
import org.example.code.repo.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VoucherService {
    @Autowired
    private VoucherRepository voucherRepository;

    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll();
    }

    public Optional<Voucher> getVoucherById(Integer id) {
        return voucherRepository.findById(id);
    }

    public Voucher addVoucher(Voucher voucher) {
        return voucherRepository.save(voucher);
    }

    public List<Voucher> getvoucherBytrangThaiHoatDong() {
        return voucherRepository.findByTrangThai("hoat_dong");
    }

    public Voucher updateVoucher(Integer id, Voucher voucher) {
        voucher.setId(id);
        return voucherRepository.save(voucher);
    }

    public void deleteVoucher(Integer id) {
        voucherRepository.deleteById(id);
    }

    public Voucher findByMaVoucher(String maVoucher) {
        return voucherRepository.findByMaVoucher(maVoucher);
    }

    // Áp dụng voucher: kiểm tra điều kiện, trả về true/false hoặc giá trị giảm
    public boolean isVoucherApplicable(Voucher voucher, double tongTien) {
//        if (voucher == null || !voucher.getTrangThai()) return false;
//        if (voucher.getSoLuongVoucher() <= 0) return false;
//        if (tongTien < voucher.getGiaTriToiThieuApDung()) return false;
        // Kiểm tra ngày hiệu lực
        java.time.LocalDate now = java.time.LocalDate.now();
//        if (now.isBefore(voucher.getNgayBatDau()) || now.isAfter(voucher.getNgayKetThuc())) return false;
        return true;
    }
}
