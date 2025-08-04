package org.example.code.controller;

import org.example.code.DTO.SanPhamChiTietDTO2;
import org.example.code.DTO.ShippingFeeRequest;
import org.example.code.model.*;
import org.example.code.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class BanHangTaiQuayController {
    @Autowired
    private BanHangTaiQuayService banHangTaiQuayService;
    @Autowired
    private HoaDonService hoaDonService;
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private SanPhamChiTietService sanPhamService;
    @Autowired
    private MauSacService mauSacService;
    @Autowired
    private PhienBanService phienBanService;
    @Autowired
    private ThuongHieuService thuongHieuService;
    @Autowired
    private XuatXuService xuatXuService;
    @Autowired
    private LoaiSanPhanService loaiSanPhamService;
    @Autowired
    private HoaDonChiTietService hoaDonChiTietService;
    @Autowired
    private NhanVienService nhanVienService;
    @GetMapping("/ban-hang-tai-quay")
    public String showBanHangTaiQuayBanHangPage(Model model,@RequestParam (value = "idhd",required = false) Integer idhd) {
        List<HoaDon> listCho = hoaDonService.getbytrangthai("CHO_THANH_TOAN"); // trạng thái CHO_THANH_TOAN
        List<HoaDonChiTiet> hoaDonChiTietList = banHangTaiQuayService.DanhSachHoaDonChiTietbyidhoadon(idhd);
        HoaDon hoaDonDangChon;

        if (idhd != null) {
            hoaDonDangChon = hoaDonService.getHoaDonById(idhd)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));
        } else if (!listCho.isEmpty()) {
            hoaDonDangChon = listCho.get(0);
            hoaDonChiTietList = banHangTaiQuayService.DanhSachHoaDonChiTietbyidhoadon(hoaDonDangChon.getId());
        } else {
            hoaDonDangChon = banHangTaiQuayService.taomoihoadoncho(); // tạo mới hóa đơn chờ
            listCho.add(hoaDonDangChon);
        }
        List<Voucher> dsVoucherDangHoatDong = voucherService.getvoucherBytrangThaiHoatDong();
        BigDecimal tongTien = hoaDonService.tinhTongTien(hoaDonDangChon);
        BigDecimal tienvoucher = voucherService.tinhTienGiam(hoaDonDangChon.getIdVoucher(), tongTien);
        BigDecimal tienShip = hoaDonDangChon.getTienship() != null ? hoaDonDangChon.getTienship() : BigDecimal.ZERO;
        System.out.println(tienShip + " tien ship");
        BigDecimal tienthanhtoan = tongTien.subtract(tienvoucher).add(tienShip);
        model.addAttribute("dsVoucherDangHoatDong", dsVoucherDangHoatDong);
        model.addAttribute("ListSanPham", banHangTaiQuayService.DanhSachSanPham()); // Replace null with actual product list if needed
        model.addAttribute("ListHoaDonChiTiet", hoaDonChiTietList); // Replace 1 with actual ID if needed
        model.addAttribute("idHoaDon", hoaDonDangChon.getId());
        model.addAttribute("hoaDon", hoaDonDangChon);
        model.addAttribute("listHoaDonCho", listCho);
        model.addAttribute("tongTien", tongTien); // Tính tổng tiền cho hóa đơn
        model.addAttribute("GiaTriThanhToan", tienthanhtoan);
        model.addAttribute("dsMauSac", mauSacService.getAll());
        model.addAttribute("dsPhienBan", phienBanService.getall());
        model.addAttribute("dsThuongHieu", thuongHieuService.getAllThuongHieus());
        model.addAttribute("dsXuatXu", xuatXuService.getAll());
        model.addAttribute("dsLoaiSanPham", loaiSanPhamService.getAllLoaiSanPhams());
        model.addAttribute("tienVoucher", tienvoucher);

        return "UI/employee/BanHangTaiQuay2";
    }

    @PostMapping("/ban-hang-tai-quay/addsp")
    public String addSanPhamToHoaDon(@RequestParam("idSanPham") Integer idSanPham,
                                      @RequestParam("soLuong") Integer soLuong,
                                      @RequestParam("idhd") Integer idHoaDon,
                                     @RequestParam(name = "nhaptay", required = false,defaultValue = "false") String nhaptay
    ) {
        Integer idhd = banHangTaiQuayService.ThemHoaDonChiTiet(idSanPham, soLuong, idHoaDon, nhaptay);
        return "redirect:/ban-hang-tai-quay?idhd=" + idhd; // Redirect back to the order page
    }

    @GetMapping("/ban-hang-tai-quay/delete/{idhd}")
    public String deleteHoaDon(@PathVariable("idhd") Integer idhd) {
        banHangTaiQuayService.XoaHoaDon(idhd);
        return "redirect:/ban-hang-tai-quay"; }// Redirect back to the main page

    @PostMapping("/ban-hang-tai-quay/thanhtoan/{idhd}")
    public String thanhToanHoaDon(@PathVariable("idhd") Integer idhd,@RequestParam String tenNguoiMua,
                                  @RequestParam String sdtNguoiMua,
                                  @RequestParam(required = false) String tenNguoiNhan,
                                  @RequestParam(required = false) String diaChiNguoiNhan,
                                  @RequestParam(required = false) String sdtNguoiNhan,
                                  @RequestParam String hinhThucThanhToan,
                                  @RequestParam(required = false) String ghiChu) {
        banHangTaiQuayService.ThanhToanHoaDon(idhd, tenNguoiMua, sdtNguoiMua, diaChiNguoiNhan
                , tenNguoiNhan, sdtNguoiNhan,hinhThucThanhToan,ghiChu);
        return "redirect:/ban-hang-tai-quay"; // Redirect back to the main page
    }

    @PostMapping("/ban-hang-tai-quay/tao-moi")
    public String taoHoaDonMoi() {
        HoaDon idhdcho = banHangTaiQuayService.taomoihoadoncho();
        return "redirect:/ban-hang-tai-quay?idhd=" + idhdcho.getId();
    }
    @PostMapping("/ban-hang-tai-quay/them-voucher")
    public String themVoucher(@RequestParam("idhd") Integer idHoaDon,
                              @RequestParam("idVoucher") Integer idVoucher) {
        hoaDonService.apDungVoucher(idHoaDon, idVoucher);
        System.out.println("Áp dụng voucher thành công cho hóa đơn ID: " + idHoaDon + " với voucher ID: " + idVoucher);
        return "redirect:/ban-hang-tai-quay?idhd=" + idHoaDon;
    }
    @GetMapping("/ban-hang-tai-quay/loc-san-pham")
    @ResponseBody
    public ResponseEntity<?> locSanPham(
            @RequestParam(required = false) Integer mauSac,
            @RequestParam(required = false) Integer phienBan,
            @RequestParam(required = false) Integer thuongHieu,
            @RequestParam(required = false) Integer xuatXu,
            @RequestParam(required = false) Integer loaiSanPham
    ) {
        List<SanPhamChiTiet> sanPhamChiTietList  = sanPhamService.locSanPham(
                thuongHieu,
                mauSac,
                phienBan,
                xuatXu,
                loaiSanPham
        );
        List<SanPhamChiTietDTO2> result = sanPhamChiTietList.stream().map(sp -> {
            SanPhamChiTietDTO2 dto = new SanPhamChiTietDTO2();
            dto.setTenMau(sp.getIdMausac().getTenMauSac());
            dto.setTenPhienBan(sp.getIdPhienban().getTenPhienBan());
            dto.setThuongHieu(sp.getIdSanpham().getIdThuonghieu().getTenThuongHieu());
            dto.setXuatXu(sp.getIdSanpham().getIdXuatxu().getNoiXuatXu());
            dto.setSoLuongTon(sp.getSoLuongTon());
            dto.setGia(sp.getDonGia());
            dto.setTensanPham(sp.getIdSanpham().getTenSanPham());
            return dto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/ban-hang-tai-quay/calculate-fee/{idhd}")
    @ResponseBody
    public BigDecimal calculateFee(@RequestBody ShippingFeeRequest request, @PathVariable Integer idhd) {
        BigDecimal result = hoaDonService.calculateShippingFee(
                Integer.parseInt(request.getProvince()),
                Integer.parseInt(request.getDistrict()),
                Integer.parseInt(request.getWard()),
                request.getWeight()
        );
        System.out.println(idhd + " idhd");

        hoaDonService.capNhatPhiShip(idhd,result);

        return result;
    }
    @GetMapping("/ban-hang-tai-quay/hoa-don/chi-tiet/{id}")
    public String chiTietHoaDon(@PathVariable Integer id, Model model) {
        Optional<HoaDon> hoaDon = hoaDonService.getHoaDonById(id);
        List<HoaDonChiTiet> chiTiets = hoaDonChiTietService.getListHoaDonChiTietByIdHoaDon(hoaDon.get().getId());
        BigDecimal tienvoucher = voucherService.tinhTienGiam(hoaDon.get().getIdVoucher(), hoaDon.get().getThanhTien());
        model.addAttribute("hoaDon", hoaDon.get());
        model.addAttribute("hoaDonChiTiets", chiTiets);
        model.addAttribute("tienVoucher", tienvoucher);

        return "UI/employee/QuanLiDonHang";
    }




}
