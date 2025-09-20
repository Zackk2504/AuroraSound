package org.example.code.controller;

import org.example.code.service.ThongKeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThongKeViewController {
    @Autowired
    private ThongKeService  thongKeService;

    @GetMapping("/admin/thong-ke")
    public String thongKePage(Model model) {
        model.addAttribute("doanhThu", thongKeService.thongKeDoanhThuTheoNgay());
        model.addAttribute("topSanPham", thongKeService.topSanPhamBanChay());
        return "admin/thongKe"; // trỏ đến file templates/thongke.html
    }
}
