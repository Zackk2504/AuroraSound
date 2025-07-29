package org.example.code.controller;

import org.example.code.model.LoaiSanPham;
import org.example.code.service.LoaiSanPhanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/loai-san-pham1")
public class LoaiSanPhanController {
    @Autowired
    private LoaiSanPhanService service;

    @GetMapping
    public String listLoaiSanPham(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {

        Page<LoaiSanPham> loaiSanPhamPage = service.getAllPaged(page, size);
        model.addAttribute("loai", new LoaiSanPham());
        model.addAttribute("dsLoaiPage", loaiSanPhamPage);
        model.addAttribute("page", page);
        model.addAttribute("totalPages", loaiSanPhamPage.getTotalPages());
        model.addAttribute("form", new LoaiSanPham());
        return "admin/loaiSanPham"; // trùng với file HTML bạn đã tạo
    }

    @PostMapping("/save")
    public String saveLoai(@ModelAttribute("form") LoaiSanPham loai) {
        service.saveLoaiSanPham(loai);
        return "redirect:/admin/loai-san-pham";
    }

    @GetMapping("/edit/{id}")
    public String editLoai(@PathVariable Integer id, Model model,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "5") int size) {
        Optional<LoaiSanPham> loai = service.getLoaiSanPhamById(id);
        Page<LoaiSanPham> loaiSanPhamPage = service.getAllPaged(page, size);
        model.addAttribute("form", loai);
        model.addAttribute("dsLoai", loaiSanPhamPage.getContent());
        model.addAttribute("page", page);
        model.addAttribute("totalPages", loaiSanPhamPage.getTotalPages());
        return "admin/loaiSanPham";
    }


}
