package org.example.code.controller;

import org.example.code.service.GioHangChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GioHangChiTietController {
    @Autowired
    GioHangChiTietService gioHangChiTietService;

    @GetMapping("/gio-hang-chi-tiet")
    public String getGioHangChiTiet(Model model) {
        try {
            model.addAttribute("gioHangChiTietList", gioHangChiTietService.getGioHangChiTietByGioHang());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi khi lấy giỏ hàng chi tiết: " + e.getMessage());
        }
        return "giohangchitiet";
    }
    @PostMapping("/gio-hang-chi-tiet/add/{sanPhamCTId}/{soLuong}")
    public ResponseEntity<String> addGioHangChiTiet(@PathVariable Integer sanPhamCTId, @PathVariable Integer soLuong) {
        try {
            gioHangChiTietService.addGioHangChiTiet(sanPhamCTId, soLuong);
            return ResponseEntity.ok("Thêm vào giỏ hàng thành công");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi khi thêm vào giỏ hàng: " + e.getMessage());
        }
    }
    @PostMapping("/gio-hang-chi-tiet/giam/{sanPhamCTId}")
    public ResponseEntity<String> giamGioHangChiTiet(@PathVariable Integer sanPhamCTId) {
        try {
            gioHangChiTietService.giamGioHangChiTiet(sanPhamCTId);
            return ResponseEntity.ok("Giảm số lượng sản phẩm trong giỏ hàng thành công");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi khi giảm số lượng sản phẩm: " + e.getMessage());
        }
    }
}
