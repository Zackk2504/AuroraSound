package org.example.code.service;

import jakarta.transaction.Transactional;
import org.example.code.model.Voucher;
import org.example.code.repo.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    public void add(Voucher voucher) {
        setTrangThaiTheoNgay(voucher);
        voucher.setSoLuongConLai((voucher.getSoLuongVoucher()));
        voucherRepository.save(voucher);
    }
    public void Update(Voucher voucher) {
        setTrangThaiTheoNgay(voucher);
        voucherRepository.save(voucher);
    }
    public void stopvoucher(Integer id){
        Voucher voucher = voucherRepository.findById(id).get();
        if (voucher == null){
            throw new RuntimeException("voucher is null");
        }
        LocalDateTime now = LocalDateTime.now().minusMinutes(1); // trừ đi 1 phút
        voucher.setNgayKetThuc(now);
        setTrangThaiTheoNgay(voucher);
        voucherRepository.save(voucher);
    }

    public List<Voucher> getvoucherBytrangThaiHoatDong() {
        return voucherRepository.findByTrangThai("DANG_DIEN_RA");
    }

    public void apdungvoucher(Integer idVoucher) {
        Voucher voucher = voucherRepository.findById(idVoucher).orElseThrow(() -> new RuntimeException("Voucher not found with id: " + idVoucher));
        if (voucher.getSoLuongConLai() <= 0) {
            throw new RuntimeException("Voucher đã hết hạn sử dụng hoặc không còn số lượng");
        }
        voucher.setSoLuongConLai(voucher.getSoLuongConLai() - 1);
        voucherRepository.save(voucher);
    }

    public Voucher updateVoucher(Integer id, Voucher voucher) {
        voucher.setId(id);
        setTrangThaiTheoNgay(voucher);
        voucher.setSoLuongConLai((voucher.getSoLuongVoucher()));
        return voucherRepository.save(voucher);
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
    public enum TrangThaiVoucher {
        SAP_DIEN_RA,
        DANG_DIEN_RA,
        DA_KET_THUC
    }

    private void setTrangThaiTheoNgay(Voucher voucher) {
        LocalDateTime now = LocalDateTime.now();

        if (voucher.getNgayBatDau().isAfter(now)) {
            voucher.setTrangThai(String.valueOf(TrangThaiVoucher.SAP_DIEN_RA));
        } else if ((voucher.getNgayBatDau().isBefore(now) || voucher.getNgayBatDau().isEqual(now))
                && (voucher.getNgayKetThuc().isAfter(now) || voucher.getNgayKetThuc().isEqual(now))) {
            voucher.setTrangThai(String.valueOf(TrangThaiVoucher.DANG_DIEN_RA));
        } else if (voucher.getNgayKetThuc().isBefore(now)) {
            voucher.setTrangThai(String.valueOf(TrangThaiVoucher.DA_KET_THUC));
        }
    }


    @Scheduled(cron = "0 * * * * ?")
    @Transactional
    public void updateVoucherStatuses() {
        LocalDateTime now = LocalDateTime.now();

        // Cập nhật voucher đã kết thúc
        voucherRepository.updateStatusForExpiredVouchers("DA_KET_THUC", now);

        // Cập nhật voucher đang diễn ra
        voucherRepository.updateStatusForActiveVouchers("DANG_DIEN_RA", now);

        // Cập nhật voucher sắp diễn ra (nếu cần)
        voucherRepository.updateStatusForUpcomingVouchers("SAP_DIEN_RA", now);
    }

    public BigDecimal tinhTienGiam(Voucher voucher, BigDecimal tongTien) {
        if (voucher == null || tongTien == null) return BigDecimal.ZERO;

        // Kiểm tra điều kiện áp dụng
        if (tongTien.compareTo(voucher.getGiaTriToiThieuApDung()) < 0) {
            return BigDecimal.ZERO; // Không đủ điều kiện áp dụng
        }

        BigDecimal tienGiam;

        if ("phan_tram".equalsIgnoreCase(voucher.getKieuGiam())) {
            // Giam theo phần trăm
            BigDecimal phanTram = voucher.getGiaTriVoucher(); // ví dụ: 10 = 10%
            tienGiam = tongTien.multiply(phanTram).divide(BigDecimal.valueOf(100));
            if (voucher.getGiaTriToiDaVoucher() != null) {
                tienGiam = tienGiam.min(voucher.getGiaTriToiDaVoucher()); // không vượt quá giá trị tối đa giảm
            }
        } else {
            // Giảm trực tiếp tiền
            tienGiam = voucher.getGiaTriVoucher();
        }
        // Không vượt quá giới hạn tối đa của voucher (nếu có)
        if (voucher.getGiaTriToiDaVoucher() != null && voucher.getGiaTriToiDaVoucher().compareTo(BigDecimal.ZERO) > 0) {
            tienGiam = tienGiam.min(voucher.getGiaTriToiDaVoucher());
        }

        // Không được giảm quá tổng tiền đơn hàng
        return tienGiam.min(tongTien);
    }
}
