package org.example.code.controller;

import org.example.code.model.GioHangChiTiet;
import org.example.code.model.HoaDon;
import org.example.code.model.KhachHang;
import org.example.code.service.GioHangChiTietService;
import org.example.code.service.HoaDonService;
import org.example.code.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller

public class KhachHangController {
    @Autowired
    private KhachHangService khachHangService;
    @Autowired
    private GioHangChiTietService gioHangChiTietService;
    @Autowired
    private HoaDonService hoaDonService;

    @GetMapping("/khach-hang/tim-theo-sdt")
    public ResponseEntity<?> getKhachHangBySoDT(@RequestParam("sdt") String sdt) {
        Optional<KhachHang> khach = khachHangService.getKhachHangBySDT(sdt);
        if (khach.isPresent()) {
            KhachHang kh = khach.get();
            return ResponseEntity.ok(kh);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy khách hàng với số điện thoại: " + sdt);
        }

    }

    @PostMapping("/khach-hang/thanh-toan")
    public String hienThiTrangThanhToan(@RequestParam("selectedIds") List<Integer> selectedIds,
                                        Model model) {
        List<GioHangChiTiet> gioHangDaChon = gioHangChiTietService.findByIds(selectedIds);
        model.addAttribute("gioHangDaChon", gioHangDaChon);
        double tongTien = gioHangDaChon.stream()
                .mapToDouble(item -> item.getSoLuong() * item.getIdSanphamchitiet().getDonGia())
                .sum();
        model.addAttribute("tongTien", tongTien);
        return "User/ThanhToan";
    }

    @PostMapping("/khach-hang/xac-nhan-thanh-toan")
    public String xacNhanThanhToan(@RequestParam("ids") List<Integer> ids) {
        HoaDon hoaDon = new HoaDon(); // hoặc lấy theo người dùng

        hoaDonService.taoHoaDonChiTiet(hoaDon, ids);

        return "redirect:/khach-hang/hoa-don/" + hoaDon.getId();
    }
}
