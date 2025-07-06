package org.example.code.controller;

import org.example.code.model.ThuongHieu;
import org.example.code.service.ThuongHieuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class ThuongHieuController {
    @Autowired
    private ThuongHieuService thuongHieuService;

    @GetMapping("/thuong-hieu")
    public String getAllThuongHieus(Model model) {
        // Logic to retrieve all Thuong Hieus and return a view
        model.addAttribute("thuongHieu", new ThuongHieu());
        model.addAttribute("dsThuongHieu", thuongHieuService.getAllThuongHieus());
        return "admin/thuongHieu"; // This should be the name of your view template
    }


    @PostMapping("/thuonghieu/add")
    public String addThuongHieu(@ModelAttribute ThuongHieu thuongHieu) {
        // Logic to save the new Thuong Hieu
        thuongHieuService.saveThuongHieu(thuongHieu);
        return "redirect:/admin/thuong-hieu"; // Redirect to the list view after adding
    }

    @GetMapping("/thuonghieu/edit/{id}")
    public String editThuongHieuForm(Model model, @PathVariable Integer id) {
        Optional<ThuongHieu> thuongHieu = thuongHieuService.getThuongHieuById(id);
        if (thuongHieu.isPresent()) {
            model.addAttribute("dsThuongHieu", thuongHieuService.getAllThuongHieus());
            model.addAttribute("thuongHieu", thuongHieu.get());
        } else {
            // Handle the case where the Thuong Hieu is not found
            model.addAttribute("error", "Thuong Hieu not found");
        }
        return "admin/thuongHieu"; // This should be the name of your view template for editing
    }
    @PostMapping("/thuonghieu/update")
    public String editThuongHieu(@ModelAttribute ThuongHieu thuongHieu) {
        // Logic to update the Thuong Hieu
        thuongHieuService.saveThuongHieu(thuongHieu);
        return "redirect:/thuong-hieu"; // Redirect to the list view after editing
    }
}
