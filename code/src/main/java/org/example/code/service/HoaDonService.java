package org.example.code.service;

import jakarta.transaction.Transactional;
import org.example.code.model.*;
import org.example.code.repo.HoaDonRepository;
import org.example.code.repo.LichSuHoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.time.LocalDateTime;
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
    @Autowired
    private NhanVienService nhanVienService;
    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;
    @Autowired
    private LichSuHoaDonRepository lichSuHoaDonRepository;


    public Optional<HoaDon> getHoaDonById(Integer id) {
        return hoaDonRepository.getHoaDonById(id);
    }
    public List<HoaDon> getAllHoaDon() {
        return hoaDonRepository.findAll();
    }

    public void addAndEdit(HoaDon hoaDon) {
        hoaDonRepository.save(hoaDon);
    }

    public List<LichSuHoaDon> getLichSuHoaDonByHoaDonId(Integer id) {
        return lichSuHoaDonRepository.findAllByHoaDon_Id(id);
    }

    public void addAndEditlshoadon(LichSuHoaDon lichSuHoaDon){
        lichSuHoaDonRepository.save(lichSuHoaDon);
    }

    public void deleteHoaDon(Integer id) {
        hoaDonRepository.deleteById(id);
    }
    public void xoahd(Integer id){
        hoaDonRepository.deleteById(id);
        hoaDonChiTietService.deleteAllByIdHoaDon(id);
    }

    public void setlshoadon(String ghiChu,String TTcu,String ttms,HoaDon hoaDon, NhanVien idnv ) {
        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
        lichSuHoaDon.setHoaDon(hoaDon);
        lichSuHoaDon.setNhanVien(idnv);
        lichSuHoaDon.setNgayCapNhat(LocalDateTime.now());
        lichSuHoaDon.setTrangThaiCu(TTcu);
        lichSuHoaDon.setTrangThaiMoi(ttms);
        lichSuHoaDon.setGhiChu(ghiChu);
        lichSuHoaDonRepository.save(lichSuHoaDon);
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

    private static final String KY_TU = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int DO_DAI_MA = 6;
    private static final SecureRandom random = new SecureRandom();

    public String taoMaHoaDon() {
        StringBuilder ma = new StringBuilder(DO_DAI_MA);
        for (int i = 0; i < DO_DAI_MA; i++) {
            int index = random.nextInt(KY_TU.length());
            ma.append(KY_TU.charAt(index));
        }
        // Đảm bảo mã hóa đơn là duy nhất
        Optional<HoaDon> existingHoaDon = hoaDonRepository.findByMaHoaDon(ma.toString());
        if (existingHoaDon.isPresent()) {
            return taoMaHoaDon(); // Gọi đệ quy nếu mã đã tồn tại
        }
        return ma.toString();
    }

    public void xacNhanDonHang(Integer idHoaDon,String ghiChu, String diaChiMoi, String tienTraSau) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String tenDangNhap = auth.getName(); // Lấy tên đăng nhập của người dùng hiện tại
        NhanVien nhanVien = nhanVienService.getNhanVienByTenDangNhap(tenDangNhap);
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với ID: " + idHoaDon));
        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
        if (hoaDon.getTrangThaiHoaDon().equals("CHO_XAC_NHAN")) {
            if (ghiChu != null && !ghiChu.isBlank()) {
                hoaDon.setGhiChu(ghiChu);
            }
            if (diaChiMoi != null && !diaChiMoi.isBlank()) {
                hoaDon.setDiaChiNhanHang(diaChiMoi);
            }
            if (tienTraSau != null && !tienTraSau.isBlank()) {
                BigDecimal tienTraSauBD = new BigDecimal(tienTraSau);
                if (hoaDon.getHinhThucThanhToan().equals("TIEN_MAT")){
                    hoaDon.setGiaTriThanhToan(hoaDon.getGiaTriThanhToan().add(tienTraSauBD));
                }else {
                    hoaDon.setTienTraSau(tienTraSauBD);
                }
            }
            hoaDon.setTrangThaiHoaDon("DA_XAC_NHAN");
            hoaDon.setIdNhanvien(nhanVien); // Cập nhật nhân viên xử lý hóa đơn
            hoaDonRepository.save(hoaDon);
//            Lưu lịch sử
            lichSuHoaDon.setHoaDon(hoaDon);
            lichSuHoaDon.setNhanVien(nhanVien);
            lichSuHoaDon.setNgayCapNhat(LocalDateTime.now());
            lichSuHoaDon.setGhiChu(ghiChu);
            lichSuHoaDon.setTrangThaiCu("");
            lichSuHoaDon.setTrangThaiMoi(hoaDon.getTrangThaiHoaDon());
            lichSuHoaDonRepository.save(lichSuHoaDon);

            List<HoaDonChiTiet> hoaDonChiTiet = hoaDonChiTietService.getListHoaDonChiTietByIdHoaDon(idHoaDon);
            if (hoaDonChiTiet.isEmpty()) {
                throw new RuntimeException("Hóa đơn không có chi tiết sản phẩm.");
            }
            for (HoaDonChiTiet chiTiet : hoaDonChiTiet) {
                SanPhamChiTiet sanPham = chiTiet.getIdSanphamchitiet();
                if (sanPham.getSoLuongTon() < chiTiet.getSoLuong()) {
                    throw new RuntimeException("Số lượng sản phẩm không đủ để xác nhận đơn hàng.");
                }
                sanPham.setSoLuongTon(sanPham.getSoLuongTon() - chiTiet.getSoLuong());
                sanPhamChiTietService.addAndEdit(sanPham);
                sanPhamChiTietService.checksoluong(sanPham.getId());
            }
        } else {
            throw new RuntimeException("Trạng thái hóa đơn không hợp lệ để xác nhận.");
        }
    }
    public Page<HoaDon> findWithFilters(String sdt, String trangThai, String loai, String hinhThuc, Pageable pageable) {
        return hoaDonRepository.findWithFilters(
                sdt == null || sdt.isBlank() ? null : sdt,
                trangThai == null || trangThai.isBlank() ? null : trangThai,
                loai == null || loai.isBlank() ? null : loai,
                hinhThuc == null || hinhThuc.isBlank() ? null : hinhThuc,
                pageable
        );
    }

    public Optional<HoaDon> getHoaDonByMa(String maHoaDon) {
        return hoaDonRepository.findByMaHoaDon(maHoaDon);
    }
    @Transactional
    public void huyDonHang(Integer idHoaDon,String ghiChu) {
        Optional<HoaDon> hoaDon = hoaDonRepository.findById(idHoaDon);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String tenDangNhap = auth.getName(); // Lấy tên đăng nhập của người dùng hiện tại
        NhanVien nhanVien = nhanVienService.getNhanVienByTenDangNhap(tenDangNhap);
        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
        if (nhanVien == null) {
            throw new RuntimeException("Không tìm thấy nhân viên với tên đăng nhập: " + tenDangNhap);
        }
        if (!hoaDon.isPresent()) {
          throw new RuntimeException("Không tìm thấy hóa đơn với ID: " + idHoaDon);
        }
        HoaDon hd = hoaDon.get();
        if (hd.getIdVoucher() != null) {
            Voucher voucher = hd.getIdVoucher();
            voucher.setSoLuongConLai(voucher.getSoLuongConLai() + 1); // Trả lại voucher
            voucherService.updateVoucher(voucher.getId(), voucher);
        }
        if (hd.getTrangThaiHoaDon().equalsIgnoreCase("CHO_XAC_NHAN")) {
            hd.setTrangThaiHoaDon("DA_HUY");
//            hd.setIdNhanvien(nhanVien); // Cập nhật nhân viên xử lý hóa đơn
            lichSuHoaDon.setTrangThaiCu("CHO_XAC_NHAN");
            lichSuHoaDon.setTrangThaiMoi("DA_HUY");
            hoaDonRepository.save(hd);
        } else {
//            Trường hợp hủy này khi khách ko nhận thì trả lại số lượng
            lichSuHoaDon.setTrangThaiCu(hoaDon.get().getTrangThaiHoaDon());
            hd.setTrangThaiHoaDon("THAT_BAI");
            lichSuHoaDon.setTrangThaiMoi("THAT_BAI");
            List<HoaDonChiTiet> hoaDonChiTiet = hoaDonChiTietService.getListHoaDonChiTietByIdHoaDon(idHoaDon);
            for (HoaDonChiTiet chiTiet : hoaDonChiTiet) {
                SanPhamChiTiet sanPham = chiTiet.getIdSanphamchitiet();
                sanPham.setSoLuongTon(sanPham.getSoLuongTon() + chiTiet.getSoLuong());
                sanPhamChiTietService.addAndEdit(sanPham);
            }
            hoaDonRepository.save(hd);
        }
        lichSuHoaDon.setHoaDon(hoaDon.get());
        lichSuHoaDon.setNhanVien(nhanVien);
        lichSuHoaDon.setGhiChu(ghiChu);
        lichSuHoaDon.setNgayCapNhat(LocalDateTime.now());
        lichSuHoaDonRepository.save(lichSuHoaDon);
    }

}
