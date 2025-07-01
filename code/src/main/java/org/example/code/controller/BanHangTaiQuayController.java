package org.example.code.controller;

import org.example.code.model.HoaDon;
import org.example.code.service.BanHangTaiQuayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BanHangTaiQuayController {
    @Autowired
    private BanHangTaiQuayService banHangTaiQuayService;

    @GetMapping("/ban-hang-tai-quay")
    public String showBanHangTaiQuayPage(Model model) {
        model.addAttribute("ListSanPham", banHangTaiQuayService.DanhSachSanPham()); // Replace null with actual product list if needed
        return "UI/employee/BanHangTaiQuay";
    }

    @GetMapping("/ban-hang-tai-quay/{idhd}")
    public String showBanHangTaiQuayBanHangPage(Model model, @PathVariable ("idhd") Integer id) {
        model.addAttribute("ListSanPham", banHangTaiQuayService.DanhSachSanPham()); // Replace null with actual product list if needed
        model.addAttribute("ListHoaDonChiTiet", banHangTaiQuayService.DanhSachHoaDonChiTiet(id)); // Replace 1 with actual ID if needed
        model.addAttribute("idHoaDon", id);
        return "UI/employee/BanHangTaiQuay2";
    }

    @PostMapping("/ban-hang-tai-quay/addsp")
    public String addSanPhamToHoaDon(@RequestParam("idSanPham") Integer idSanPham,
                                      @RequestParam("soLuong") Integer soLuong,
                                      @RequestParam("idhd") Integer idHoaDon,
                                     @RequestParam(name = "nhaptay", required = false,defaultValue = "false") String nhaptay
    ) {
        Integer idhd = banHangTaiQuayService.ThemHoaDonChiTiet(idSanPham, soLuong, idHoaDon, nhaptay);

        return "redirect:/ban-hang-tai-quay/" + idhd; // Redirect back to the order page
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
}
