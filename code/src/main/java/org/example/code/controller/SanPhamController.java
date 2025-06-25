package org.example.code.controller;

import org.example.code.DTO.SanPhamChiTietDTO;
import org.example.code.DTO.SanPhamDTO;
import org.example.code.model.SanPham;
import org.example.code.model.SanPhamChiTiet;
import org.example.code.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SanPhamController {
    @Autowired
    private SanPhamService sanPhamService;

    @GetMapping("/san-pham")
    public String getSanPham(Model model) {
        model.addAttribute("sanphamlist", sanPhamService.getAllSanPhams());
        return "sanpham"; // This should match the name of your view template
    }
    @GetMapping("/sanpham/add")
    public String addSanPham(Model model) {

        return "add_sanpham";
    }

    @PostMapping("/sanpham/add")
    public ResponseEntity<?> saveSanPham(@ModelAttribute SanPhamDTO dto) {
        sanPhamService.addSp(dto);
//        return "redirect:/sanpham"; // Redirect to the list after saving
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/sanpham/edit/{id}")
    public ResponseEntity<?> editSanPham(Model model,@PathVariable Integer id,
                              @ModelAttribute SanPhamDTO sanPhamDTO) {
        System.out.println("Editing SanPham with ID: " + id);
        SanPham sanPham = sanPhamService.getAllSanPhams().stream()
                .filter(sp -> sp.getId().equals(id))
                .findFirst()
                .orElse(null);
        model.addAttribute("sanpham",sanPham );
       return ResponseEntity.ok(sanPham);
    }

    @PostMapping("/sanpham/edit")
    public ResponseEntity<?> updateSanPham(@ModelAttribute SanPhamDTO dto) {
        sanPhamService.addSp(dto);
        return ResponseEntity.ok(dto); // Return the updated DTO or a success message
    }
    
}
