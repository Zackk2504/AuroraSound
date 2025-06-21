package org.example.code.controller;

import org.example.code.model.XuatXu;
import org.example.code.service.XuatXuService;
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
public class XuatXuController {
    @Autowired
    private XuatXuService xuatXuService;

    @GetMapping("/xuat-xu")
    public String getAllXuatXu(Model model) {
        model.addAttribute("xuatXuList", xuatXuService.getAll());
        return "xuat-xu-list"; // This should be the name of your view template
    }

    @GetMapping("/xuat-xu/add")
    public String addXuatXuForm(Model model) {
        return "xuat-xu-add"; // This should be the name of your view template for adding
    }

    @PostMapping("/xuat-xu/add")
    public String addXuatXu(@ModelAttribute XuatXu xuatXu) {
        // Logic to save the new Xuat Xu
        xuatXuService.save(xuatXu);
        return "redirect:/xuat-xu"; // Redirect to the list view after adding
    }

    @GetMapping("/xuat-xu/edit?id={id}")
    public String editXuatXuForm(Model model, @RequestParam Integer id) {
        Optional<XuatXu> xuatXu = xuatXuService.findByID(id);
        if (xuatXu.isPresent()) {
            model.addAttribute("xuatXu", xuatXu.get());
        } else {
            model.addAttribute("error", "Xuat Xu not found");
        }
        return "xuat-xu-edit"; // This should be the name of your view template for editing
    }

    @PostMapping("/xuat-xu/edit?id={id}")
    public String editXuatXu(@ModelAttribute XuatXu xuatXu, @RequestParam Integer id) {
        // Logic to update the Xuat Xu
        Optional<XuatXu> existingXuatXu = xuatXuService.findByID(id);
        if (existingXuatXu.isEmpty()) {
            return "redirect:/xuat-xu?error=not_found"; // Redirect with an error message
        }
        XuatXu xuatXu1 = existingXuatXu.get();
        xuatXu1.setNoiXuatXu(xuatXu.getNoiXuatXu());
        xuatXu1.setMaXuatXu(xuatXu.getMaXuatXu());
        xuatXu1.setTrangThai(xuatXu.getTrangThai());
        xuatXuService.save(xuatXu1);
        return "redirect:/xuat-xu"; // Redirect to the list view after editing
    }
}
