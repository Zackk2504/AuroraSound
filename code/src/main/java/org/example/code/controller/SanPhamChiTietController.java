    package org.example.code.controller;

    import org.example.code.model.MauSac;
    import org.example.code.model.PhienBan;
    import org.example.code.model.SanPham;
    import org.example.code.model.SanPhamChiTiet;
    import org.example.code.service.SanPhamChiTietService;
    import org.example.code.service.SanPhamService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.RequestMapping;

    import java.util.*;

    @Controller
    @RequestMapping("/khach-hang")
    public class SanPhamChiTietController {
        @Autowired
        private SanPhamService sanPhamService;
        @Autowired
        private SanPhamChiTietService sanPhamChiTietService;
        @GetMapping("/san-pham/{id}")
        public String chiTietSanPham(@PathVariable Integer id, Model model) {
            Optional<SanPham> sanPham = sanPhamService.getSanPhamById(id);
            if (sanPham.isEmpty()) {
                return "404"; // Không tìm thấy
            }

            List<SanPhamChiTiet> sanPhamChiTietList = sanPhamChiTietService.getBySanPhamId(id);

            // Dùng Set để tự động loại bỏ trùng lặp
            Set<MauSac> mauSacSet = new HashSet<>();
            Set<PhienBan> phienBanSet = new HashSet<>();

            for (SanPhamChiTiet spct : sanPhamChiTietList) {
                mauSacSet.add(spct.getIdMausac());
                phienBanSet.add(spct.getIdPhienban());
            }

            model.addAttribute("sp", sanPham.get());
            model.addAttribute("spChiTiet", sanPhamChiTietList);
            model.addAttribute("mauSacList", new ArrayList<>(mauSacSet));
            model.addAttribute("phienBanList", new ArrayList<>(phienBanSet));

            return "User/SanPham"; // trang thymeleaf
        }

    }
