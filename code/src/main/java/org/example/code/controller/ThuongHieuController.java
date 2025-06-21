package org.example.code.controller;

import org.example.code.model.ThuongHieu;
import org.example.code.service.ThuongHieuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class ThuongHieuController {
    @Autowired
    private ThuongHieuService thuongHieuService;

    @GetMapping("/thuong-hieu")
    public String getAllThuongHieus(Model model) {
        // Logic to retrieve all Thuong Hieus and return a view
        model.addAttribute("thuongHieulist", thuongHieuService.getAllThuongHieus());
        return "thuong-hieu-list"; // This should be the name of your view template
    }

    @GetMapping("/thuong-hieu/add")
    public String addThuongHieuForm(Model model) {
        return "thuong-hieu-add"; // This should be the name of your view template for adding
    }

    @PostMapping("/thuong-hieu/add")
    public String addThuongHieu(@ModelAttribute ThuongHieu thuongHieu) {
        // Logic to save the new Thuong Hieu
        thuongHieuService.saveThuongHieu(thuongHieu);
        return "redirect:/thuong-hieu"; // Redirect to the list view after adding
    }

    @GetMapping("/thuong-hieu/edit?id={id}")
    public String editThuongHieuForm(Model model, @RequestParam Integer id) {
        Optional<ThuongHieu> thuongHieu = thuongHieuService.getThuongHieuById(id);
        if (thuongHieu.isPresent()) {
            model.addAttribute("thuongHieu", thuongHieu.get());
        } else {
            // Handle the case where the Thuong Hieu is not found
            model.addAttribute("error", "Thuong Hieu not found");
        }
        return "thuong-hieu-edit"; // This should be the name of your view template for editing
    }
    @PostMapping("/thuong-hieu/edit?id={id}")
    public String editThuongHieu(@ModelAttribute ThuongHieu thuongHieu,@RequestParam Integer id) {
        // Logic to update the Thuong Hieu
        Optional<ThuongHieu> existingThuongHieu = thuongHieuService.getThuongHieuById(id);
        if (existingThuongHieu.isEmpty()) {
            // Handle the case where the Thuong Hieu is not found
            return "redirect:/thuong-hieu?error=not_found"; // Redirect with an error message
        }
        ThuongHieu thuongHieu1 = existingThuongHieu.get();
        thuongHieu1.setTenThuongHieu(thuongHieu.getTenThuongHieu());
        thuongHieu1.setMaThuongHieu(thuongHieu.getMaThuongHieu());
        thuongHieu1.setTrangThai(thuongHieu.getTrangThai());
        thuongHieuService.saveThuongHieu(thuongHieu);
        return "redirect:/thuong-hieu"; // Redirect to the list view after editing
    }
}
