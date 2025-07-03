package org.example.code.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.code.model.NhanVien;
import org.example.code.model.VaiTro;
import org.example.code.service.NhanVienService;
import org.example.code.service.VaiTroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class NhanVienController {
    @Autowired
    private NhanVienService nhanVienService;

    @Autowired
    private VaiTroService vaiTroService;

    @GetMapping("/nhan-vien/login")
    public String login() {
        return "UI/employee/login";
    }

    @GetMapping("/nhan-vien/logout")
    public String logout() {
        return "redirect:/nhan-vien/login"; // Redirect to login page after logout
    }
    @GetMapping("/nhan-vien/home")
    public String home(Model model) {
        model.addAttribute("ListnhanVien", nhanVienService.getAll());
        return "admin/Index"; // Redirect to employee home page
    }

    @PostMapping("/admin/nhan-vien/create")
    public String createNhanVien(Model model, @ModelAttribute ("NhanVien") NhanVien nhanVien) {
        nhanVienService.themNhanVien(nhanVien);
        return "redirect:/admin/nhan-vien"; // Redirect to employee creation page
    }

    @GetMapping("/admin/nhan-vien")
    public String home(HttpServletRequest request, Model model, @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "5") int size) {
        Page<NhanVien> nhanVienPage = nhanVienService.getAll(PageRequest.of(page, size));
        model.addAttribute("nhanVienPage", nhanVienPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", nhanVienPage.getTotalPages());
        model.addAttribute("nhanVien", new NhanVien());
        model.addAttribute("danhSachVaiTro", vaiTroService.getAllVaiTros());
        return "admin/nhanVien";
    }

    @PostMapping("/admin/nhan-vien/edit")
    public String updateNhanVien(@ModelAttribute NhanVien nhanVien) {
        Optional<NhanVien> nhanVien1 = nhanVienService.getById(nhanVien.getId());
        VaiTro vaiTro = vaiTroService.getVaiTroByName("employee");
        if (nhanVien1.isPresent()){
            NhanVien existingNhanVien = nhanVien1.get();
            existingNhanVien.setHoTen(nhanVien.getHoTen());
            existingNhanVien.setGioiTinh(nhanVien.getGioiTinh());
            existingNhanVien.setSoDT(nhanVien.getSoDT());
            existingNhanVien.setTrangThai(nhanVien.getTrangThai());
            existingNhanVien.setNgaySinh(nhanVien.getNgaySinh());
            existingNhanVien.setDiaChi(nhanVien.getDiaChi());
            existingNhanVien.setVaiTro(vaiTro);
            nhanVienService.addAndUpdate(existingNhanVien);
            return "redirect:/admin/nhan-vien";
        }
        return "admin/nhanVien";
    }
}
