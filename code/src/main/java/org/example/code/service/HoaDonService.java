package org.example.code.service;

import jakarta.transaction.Transactional;
import org.example.code.model.GioHangChiTiet;
import org.example.code.model.HoaDon;
import org.example.code.model.HoaDonChiTiet;
import org.example.code.model.Voucher;
import org.example.code.repo.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class HoaDonService {
    @Autowired
    private HoaDonRepository hoaDonRepository;
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private HoaDonChiTietService hoaDonChiTietService;
    @Autowired
    private GioHangChiTietService gioHangChiTietService;

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
        if (voucher.getSoLuongConLai() <= 0) {
            throw new RuntimeException("Voucher đã hết hạn sử dụng hoặc không còn số lượng");
        }
        hoaDon.setIdVoucher(voucher);
        hoaDonRepository.save(hoaDon);
    }

    public BigDecimal tinhTongTien(HoaDon hoaDon) {
        BigDecimal tong = BigDecimal.ZERO;

        List<HoaDonChiTiet> chiTietList = hoaDonChiTietService.getListHoaDonChiTietByIdHoaDon(hoaDon.getId());

        for (HoaDonChiTiet ct : chiTietList) {
            BigDecimal soLuong = BigDecimal.valueOf(ct.getSoLuong()); // Chuyển sang BigDecimal
            BigDecimal donGia = ct.getDonGia();
            tong = tong.add(soLuong.multiply(donGia));
        }

        return tong.setScale(0, RoundingMode.HALF_UP); // Làm tròn tiền về đơn vị VNĐ
    }

    @Transactional
    public void taoHoaDonChiTiet(HoaDon hoaDon, List<Integer> idGioHangChiTiet) {
        for (Integer id : idGioHangChiTiet) {
            GioHangChiTiet ghct = gioHangChiTietService.getGioHangChiTietById(id);
            if (ghct != null) {
                HoaDonChiTiet hdct = new HoaDonChiTiet();
                hdct.setIdHoadon(hoaDon);
                hdct.setIdSanphamchitiet(ghct.getIdSanphamchitiet());
                hdct.setSoLuong(ghct.getSoLuong());
                hdct.setDonGia(ghct.getIdSanphamchitiet().getDonGia());

                hoaDonChiTietService.addAndEdit(hdct);

                gioHangChiTietService.xoaGioHangChiTiet(ghct.getId()); // Xóa khỏi giỏ
            }
        }
    }

    // Giả lập bảng giá vận chuyển
    private final Map<Integer, Integer> provinceShippingFee = Map.of(
            1, 15000, // Hà Nội
            79, 15000, // Hồ Chí Minh
            48, 25000  // Đà Nẵng
            // Thêm các tỉnh khác nếu cần
    );

    private final int DEFAULT_FEE = 30000; // Phí mặc định cho tỉnh khác

    public BigDecimal calculateShippingFee(Integer provinceCode, Integer districtCode, Integer wardCode, int weight) {


        // Kiểm tra địa chỉ hợp lệ
        if (provinceCode == null || districtCode == null || wardCode == null) {
            throw new IllegalArgumentException("Mã tỉnh, quận, huyện hoặc phường/xã không được để trống");
        }
        System.out.println("Province Code: " + provinceCode + ", District Code: " + districtCode + ", Ward Code: " + wardCode + ", Weight: " + weight);

        // Tính phí theo tỉnh (logic đơn giản)
        int baseFee = provinceShippingFee.getOrDefault(provinceCode, DEFAULT_FEE);

        // Phí bảo hiểm (1% giá trị đơn, tối thiểu 2000đ)
        int insuranceFee = Math.max(2000, (int)(baseFee * 0.01));

        // Tổng phí
        int totalFee = baseFee + insuranceFee;

        return BigDecimal.valueOf(totalFee).setScale(0, RoundingMode.HALF_UP);
    }
    public void capNhatPhiShip(Integer hoaDonId, BigDecimal phiShip) {
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));
        hoaDon.setTienship(phiShip);
        hoaDonRepository.save(hoaDon);
    }
}
