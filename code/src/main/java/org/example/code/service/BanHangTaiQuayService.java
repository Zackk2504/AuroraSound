package org.example.code.service;

import jakarta.transaction.Transactional;
import org.example.code.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BanHangTaiQuayService {
    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;

    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private HoaDonChiTietService hoaDonChiTietService;

    @Autowired
    private NhanVienService nhanVienService;

    @Autowired
    private KhachHangService khachHangService;

    @Autowired
    private VoucherService voucherService;


    public List<SanPhamChiTiet> DanhSachSanPham() {
        return sanPhamChiTietService.getAllSanPhamChiTiet();
    }
    public List<HoaDonChiTiet> DanhSachHoaDonChiTietbyidhoadon(Integer idhd) {
        return hoaDonChiTietService.getListHoaDonChiTietByIdHoaDon(idhd);
    }

    public Integer ThemHoaDonChiTiet(Integer idsp,Integer soLuong, Integer idHoaDon,String nhaptay) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietService.getSanPhamChiTietById(idsp)
                .orElseThrow(() -> new RuntimeException("SanPhamChiTiet not found"));
        if (idHoaDon == 0) {
            if (sanPhamChiTiet.getSoLuongTon() < 1) {
                throw new RuntimeException("So luong san pham khong du");
            }
            HoaDon hoaDon = new HoaDon();
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
            hoaDonChiTiet.setIdHoadon(hoaDon);
            hoaDonChiTiet.setIdSanphamchitiet(sanPhamChiTiet);
            hoaDonChiTiet.setSoLuong(1);
            hoaDonChiTiet.setDonGia(sanPhamChiTiet.getDonGia());
            sanPhamChiTiet.setSoLuongTon(sanPhamChiTiet.getSoLuongTon() - 1);
            hoaDonService.addAndEdit(hoaDon);
            hoaDonChiTietService.addAndEdit(hoaDonChiTiet);
            sanPhamChiTietService.addAndEdit(sanPhamChiTiet);
            sanPhamChiTietService.checksoluong(sanPhamChiTiet.getId());
            return hoaDon.getId();
        }else {
            Optional<HoaDon> hoaDonOptional = hoaDonService.getHoaDonById(idHoaDon);
            if (hoaDonOptional.isPresent()) {
                HoaDon hoaDon = hoaDonOptional.get();
                Optional<HoaDonChiTiet> hoaDonChiTiet = hoaDonChiTietService.getHoaDonChiTietByIdSanPhamAndIdHoaDon(idsp, idHoaDon);
                if (hoaDonChiTiet.isPresent()) {
                    HoaDonChiTiet existingHoaDonChiTiet = hoaDonChiTiet.get();
                    int oldSoLuong = existingHoaDonChiTiet.getSoLuong();
                    int newSoLuong = oldSoLuong + soLuong;
                    int chenhLech = newSoLuong - oldSoLuong;
                    System.out.println("Chenh lech: " + chenhLech);

                    if (nhaptay.equals("true")) {
                        if (soLuong <= 0) {
                            hoaDonChiTietService.delete(existingHoaDonChiTiet.getId());
                        } else {
                            if (sanPhamChiTiet.getSoLuongTon() < chenhLech) {
                                throw new RuntimeException("Không đủ hàng tồn");
                            }
                            chenhLech = soLuong - oldSoLuong;
                            existingHoaDonChiTiet.setSoLuong(soLuong);
                            sanPhamChiTiet.setSoLuongTon(sanPhamChiTiet.getSoLuongTon() - chenhLech);
                            System.out.println("so luong sp trong hd " + existingHoaDonChiTiet.getSoLuong());
                            System.out.println("chenh lech " + chenhLech);
                            System.out.println("cai tru vao so luong ton " + (sanPhamChiTiet.getSoLuongTon() - chenhLech));
                            hoaDonChiTietService.addAndEdit(existingHoaDonChiTiet);
                            sanPhamChiTietService.addAndEdit(sanPhamChiTiet);
                        }
                    }else{
                    if (newSoLuong <= 0) {
                        hoaDonChiTietService.delete(existingHoaDonChiTiet.getId());
                    } else {
                        if (sanPhamChiTiet.getSoLuongTon() < chenhLech) {
                            throw new RuntimeException("So luong san pham khong du");
                        }
                        existingHoaDonChiTiet.setSoLuong(newSoLuong);
                        sanPhamChiTiet.setSoLuongTon(sanPhamChiTiet.getSoLuongTon() - chenhLech);
                        hoaDonChiTietService.addAndEdit(existingHoaDonChiTiet);
                        sanPhamChiTietService.addAndEdit(sanPhamChiTiet);
                    }
                    }
                    sanPhamChiTietService.checksoluong(sanPhamChiTiet.getId());
                } else {
                    if (sanPhamChiTiet.getSoLuongTon() < 1) {
                        throw new RuntimeException("So luong san pham khong du");
                    }
                    HoaDonChiTiet newHoaDonChiTiet = new HoaDonChiTiet();
                    newHoaDonChiTiet.setIdHoadon(hoaDon);
                    newHoaDonChiTiet.setIdSanphamchitiet(sanPhamChiTiet);
                    newHoaDonChiTiet.setSoLuong(1);
                    newHoaDonChiTiet.setDonGia(sanPhamChiTiet.getDonGia());
                    sanPhamChiTiet.setSoLuongTon(sanPhamChiTiet.getSoLuongTon() - 1);
                    hoaDonChiTietService.addAndEdit(newHoaDonChiTiet);
                    sanPhamChiTietService.addAndEdit(sanPhamChiTiet);
                    sanPhamChiTietService.checksoluong(sanPhamChiTiet.getId());
                }

                return hoaDon.getId();
            } else {
                throw new RuntimeException("Hoa Don not found");
            }
        }

    }
    @Transactional
    public void XoaHoaDon(Integer idhd) {
        HoaDon hoaDon = hoaDonService.getHoaDonById(idhd)
                .orElseThrow(() -> new RuntimeException("Hoa Don not found"));
        List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietService.getListHoaDonChiTietByIdHoaDon(idhd);
        for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTietList) {
            SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getIdSanphamchitiet();
            sanPhamChiTiet.setSoLuongTon(sanPhamChiTiet.getSoLuongTon() + hoaDonChiTiet.getSoLuong());
            sanPhamChiTietService.addAndEdit(sanPhamChiTiet);
            hoaDonChiTietService.delete(hoaDonChiTiet.getId());
        }
        hoaDonService.deleteHoaDon(hoaDon.getId());

    }

    public void ThanhToanHoaDon(Integer idhd,String tenKhach,String sdtkhach,String diaChiNguoiNhan,String tenNguoiNhan,String sdtNguoiNhan, String hinhThucThanhToan,String ghiChu ) {
        List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietService.getListHoaDonChiTietByIdHoaDon(idhd);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String tenDangNhap = auth.getName();
        String trangThaiHoaDon = "DA_XAC_NHAN";
        BigDecimal tienShip = BigDecimal.ZERO;
        HoaDon hoaDon = hoaDonService.getHoaDonById(idhd)
                .orElseThrow(() -> new RuntimeException("Hoa Don not found"));

        NhanVien nhanVien = nhanVienService.getNhanVienByTenDangNhap(tenDangNhap);
        if (tenDangNhap == null || tenDangNhap.isEmpty()) {
            throw new RuntimeException("Ten dang nhap khong hop le");
        }

        if (tenNguoiNhan == null || tenNguoiNhan.trim().isEmpty()) {
            tenNguoiNhan = tenKhach;
        }
        if (diaChiNguoiNhan == null || diaChiNguoiNhan.trim().isEmpty()) {
            diaChiNguoiNhan = "CUA_HANG"; // hoặc diaChi nếu muốn dùng địa chỉ khách
        }
        if (sdtNguoiNhan == null || sdtNguoiNhan.trim().isEmpty()) {
            sdtNguoiNhan = sdtkhach;
        }
        if ("CUA_HANG".equals(diaChiNguoiNhan)
                && (sdtNguoiNhan.equals(sdtkhach) || sdtNguoiNhan.isEmpty())
                && (tenNguoiNhan.equals(tenKhach) || tenNguoiNhan.isEmpty())) {
            trangThaiHoaDon = "THANH_CONG";
            hoaDonService.setlshoadon(ghiChu,"DA_XAC_NHAN","THANH_CONG",hoaDon,nhanVien);
        }

        hoaDon.setTrangThaiHoaDon(trangThaiHoaDon);
        if (hoaDonChiTietList.isEmpty()) {
            throw new RuntimeException("Hoa Don Chi Tiet is empty");
        }

        BigDecimal Tongtien = BigDecimal.ZERO;
        for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTietList) {
            BigDecimal soLuong = BigDecimal.valueOf(hoaDonChiTiet.getSoLuong());
            BigDecimal donGia = hoaDonChiTiet.getDonGia();
            Tongtien = Tongtien.add(donGia.multiply(soLuong));
        }
        Voucher voucher = hoaDon.getIdVoucher();
        if (voucher != null) {
            if (voucher.getSoLuongConLai() <= 0) {
                throw new RuntimeException("Voucher đã hết hạn sử dụng hoặc không còn số lượng");
            }
            voucher.setSoLuongConLai(voucher.getSoLuongConLai() - 1);
            voucherService.Update(voucher);
        }
        BigDecimal tongTien = hoaDonService.tinhTongTien(hoaDon);
        BigDecimal tienvoucher = voucherService.tinhTienGiam(hoaDon.getIdVoucher(), tongTien);
        tienShip = hoaDon.getTienship() != null ? hoaDon.getTienship() : BigDecimal.ZERO;
        BigDecimal tienthanhtoan = tongTien.subtract(tienvoucher).add(tienShip);
        hoaDon.setIdNhanvien(nhanVien);
        hoaDon.setTenNguoiNhan(tenNguoiNhan);
        hoaDon.setMaHoaDon(hoaDonService.taoMaHoaDon());
        hoaDon.setSdtNguoiNhan(sdtNguoiNhan);
        hoaDon.setDiaChiNhanHang(diaChiNguoiNhan);
        hoaDon.setThanhTien(hoaDonService.tinhTongTien(hoaDon));
        hoaDon.setGiaTriThanhToan(tienthanhtoan);
        hoaDon.setHinhThucThanhToan(hinhThucThanhToan);
        hoaDon.setLoaiHoaDon("OFFLINE");
        hoaDon.setTenNguoiMua(tenKhach);
        hoaDon.setSdtNguoiMua(sdtkhach);
        hoaDon.setNgayTao(java.time.LocalDateTime.now());
        hoaDon.setGhiChu(ghiChu);
        hoaDonService.addAndEdit(hoaDon);

    }

    public HoaDon taomoihoadoncho(){
        HoaDon hoaDon = new HoaDon();
        hoaDon.setTrangThaiHoaDon("CHO_THANH_TOAN");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String tenDangNhap = auth.getName();
        NhanVien nhanVien = nhanVienService.getNhanVienByTenDangNhap(tenDangNhap);
        if (nhanVien == null) {
            throw new RuntimeException("Nhan vien khong ton tai");
        }
        hoaDon.setIdNhanvien(nhanVien);
        hoaDonService.addAndEdit(hoaDon);
        return hoaDon;
    }

    @Transactional
    public void xoaHoaDonChoCu() {
        List<HoaDon> danhSachCho = hoaDonService.getbytrangthai("CHO_THANH_TOAN");
        for (HoaDon hd : danhSachCho) {
            List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietService.getListHoaDonChiTietByIdHoaDon(hd.getId());
            for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTietList) {
                SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getIdSanphamchitiet();
                sanPhamChiTiet.setSoLuongTon(sanPhamChiTiet.getSoLuongTon() + hoaDonChiTiet.getSoLuong());
                sanPhamChiTietService.addAndEdit(sanPhamChiTiet);
                hoaDonChiTietService.delete(hoaDonChiTiet.getId());
            }
            hoaDonService.deleteHoaDon(hd.getId());
        }
    }

    @Scheduled(cron = "0 59 23 * * *")
    public void xoaHoaDonChoCuoiNgay() {
        this.xoaHoaDonChoCu();
    }

}
