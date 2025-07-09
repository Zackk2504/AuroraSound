package org.example.code.service;

import org.example.code.model.HoaDon;
import org.example.code.model.HoaDonChiTiet;
import org.example.code.model.Voucher;
import org.example.code.repo.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class HoaDonService {
    @Autowired
    private HoaDonRepository hoaDonRepository;
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private HoaDonChiTietService hoaDonChiTietService;

    public Optional<HoaDon> getHoaDonById(Integer id) {
        return hoaDonRepository.getHoaDonById(id);
    }
    public List<HoaDon> getAllHoaDon() {
        return hoaDonRepository.findAll();
    }

    public void addAndEdit(HoaDon hoaDon) {
        hoaDonRepository.save(hoaDon);
    }

    public void deleteHoaDon(Integer id) {
        hoaDonRepository.deleteById(id);
    }

    public List<HoaDon> getbytrangthai(String trangThaiHoaDon) {
        return hoaDonRepository.findByTrangThaiHoaDon(trangThaiHoaDon);
    }
    public void apDungVoucher(Integer idHoaDon, Integer idVoucher) {
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon).orElseThrow();
        Voucher voucher = voucherService.getVoucherById(idVoucher).orElseThrow();
        hoaDon.setIdVoucher(voucher);
        hoaDonRepository.save(hoaDon);
    }

    public BigDecimal tinhThanhTien(HoaDon hoaDon) {
        BigDecimal tong = BigDecimal.ZERO;

        List<HoaDonChiTiet> chiTietList = hoaDonChiTietService.getListHoaDonChiTietByIdHoaDon(hoaDon.getId());

        for (HoaDonChiTiet ct : chiTietList) {
            BigDecimal soLuong = BigDecimal.valueOf(ct.getSoLuong()); // Chuyển sang BigDecimal
            BigDecimal donGia = BigDecimal.valueOf(ct.getDonGia());
            tong = tong.add(soLuong.multiply(donGia));
        }

        if (hoaDon.getIdVoucher() != null) {
            Voucher voucher = hoaDon.getIdVoucher();
            BigDecimal giam = BigDecimal.ZERO;

            if ("PHAN_TRAM".equalsIgnoreCase(voucher.getKieuGiam())) {
                BigDecimal tyLeGiam = BigDecimal.valueOf(voucher.getGiaTriVoucher());
                giam = tong.multiply(tyLeGiam)
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            } else if ("TIEN_MAT".equalsIgnoreCase(voucher.getKieuGiam())) {
                giam = BigDecimal.valueOf(voucher.getGiaTriVoucher());
            }

            tong = tong.subtract(giam);

            // Đảm bảo không âm
            if (tong.compareTo(BigDecimal.ZERO) < 0) {
                tong = BigDecimal.ZERO;
            }
        }

        return tong.setScale(0, RoundingMode.HALF_UP); // Làm tròn tiền về đơn vị VNĐ
    }

}
