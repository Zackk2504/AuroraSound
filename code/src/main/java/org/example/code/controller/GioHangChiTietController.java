package org.example.code.controller;

import org.example.code.service.GioHangChiTietService;
import org.example.code.service.GioHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/khach-hang")
public class GioHangChiTietController {
    @Autowired
    GioHangChiTietService gioHangChiTietService;
    @Autowired
    private GioHangService gioHangService;

    @GetMapping("/gio-hang")
    public String getGioHangChiTiet(Model model) {
        try {
            model.addAttribute("gioHangChiTietList", gioHangChiTietService.getGioHangChiTietByGioHang());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi khi lấy giỏ hàng chi tiết: " + e.getMessage());
        }
        return "User/GioHang";
    }
    @PostMapping("/gio-hang/add/{sanPhamCTId}/{soLuong}")
    public ResponseEntity<String> addGioHangChiTiet(@PathVariable Integer sanPhamCTId, @PathVariable Integer soLuong) {
        try {
            gioHangChiTietService.addGioHangChiTiet(sanPhamCTId, soLuong);
            return ResponseEntity.ok("Thêm vào giỏ hàng thành công");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi khi thêm vào giỏ hàng: " + e.getMessage());
        }
    }

    @GetMapping("/gio-hang/so-luong")
    @ResponseBody
    public Long laySoLuongGioHang() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !authentication.getPrincipal().equals("anonymousUser")) {
            String username = authentication.getName();
            return gioHangService.demTongSanPhamTrongGio(username);
        }
        return 0L;
    }

    @PostMapping("/gio-hang/{id}/increase")
    public ResponseEntity<?> tangSoLuong(@PathVariable Integer id) {
        gioHangChiTietService.thaydoisoluongGioHangChiTiet(id,1);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/gio-hang/{id}/decrease")
    public ResponseEntity<?> giamSoLuong(@PathVariable Integer id) {
        gioHangChiTietService.thaydoisoluongGioHangChiTiet(id,-1);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/gio-hang/xoa")
    public String xoaGioHangChiTiet(Model model,@RequestParam(value = "idGioHang", required = false) Integer idGioHangct) {
        try {
            gioHangChiTietService.xoaGioHangChiTiet(idGioHangct);
            System.out.println("idGioHangct = " + idGioHangct);
            model.addAttribute("successMessage", "Xóa sp thành công");
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa giỏ hàng: " + e.getMessage());
            model.addAttribute("errorMessage", "Lỗi khi xóa giỏ hàng: " + e.getMessage());
        }
        return "redirect:/khach-hang/gio-hang";
    }
}
