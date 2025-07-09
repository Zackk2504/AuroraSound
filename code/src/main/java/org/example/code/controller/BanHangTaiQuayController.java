package org.example.code.controller;

import org.example.code.model.HoaDon;
import org.example.code.model.HoaDonChiTiet;
import org.example.code.model.Voucher;
import org.example.code.service.BanHangTaiQuayService;
import org.example.code.service.HoaDonService;
import org.example.code.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BanHangTaiQuayController {
    @Autowired
    private BanHangTaiQuayService banHangTaiQuayService;
    @Autowired
    private HoaDonService hoaDonService;
    @Autowired
    private VoucherService voucherService;

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
        model.addAttribute("dsVoucherDangHoatDong", dsVoucherDangHoatDong);
        model.addAttribute("ListSanPham", banHangTaiQuayService.DanhSachSanPham()); // Replace null with actual product list if needed
        model.addAttribute("ListHoaDonChiTiet", hoaDonChiTietList); // Replace 1 with actual ID if needed
        model.addAttribute("idHoaDon", hoaDonDangChon.getId());
        model.addAttribute("hoaDon", hoaDonDangChon);
        model.addAttribute("listHoaDonCho", listCho);
        model.addAttribute("tongTien", hoaDonService.tinhThanhTien(hoaDonDangChon)); // Tính tổng tiền cho hóa đơn

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
                                  @RequestParam String hinhThucThanhToan) {
        banHangTaiQuayService.ThanhToanHoaDon(idhd, tenNguoiMua, sdtNguoiMua, diaChiNguoiNhan
                , tenNguoiNhan, sdtNguoiNhan,hinhThucThanhToan);
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
        return "redirect:/ban-hang-tai-quay?idhd=" + idHoaDon;
    }
}
