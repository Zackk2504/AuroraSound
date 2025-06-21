package org.example.code.controller;

import org.example.code.model.PhienBan;
import org.example.code.service.PhienBanService;
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
public class PhienBanController {
    @Autowired
    PhienBanService phienBanService;

    @GetMapping("/phienban")
    public String getPhienBan(Model model) {
        model.addAttribute("phienbanlist", phienBanService.getall());
        return "phienban"; // This should correspond to a view template named 'phienban'
    }

    @GetMapping("/phienban/add")
    public String addPhienBan(Model model) {
        return "add_phienban";
    }

    @PostMapping("/phienban/add")
    public String savePhienBan(@ModelAttribute PhienBan phienBan) {
        phienBanService.save(phienBan);
        return "redirect:/phienban"; // Redirect to the list after saving
    }

    @GetMapping("/phienban/edit?id={id}")
    public String editPhienBan(Model model,@RequestParam Integer id) {
        PhienBan phienBan = phienBanService.getById(id);
        if (phienBan != null) {
            model.addAttribute("phienban", phienBan);
            return "edit_phienban"; // This should correspond to a view template named 'edit_phienban'
        } else {
            return "redirect:/phienban"; // Redirect if not found
        }
    }

    @PostMapping("/phienban/edit?id={id}")
    public String updatePhienBan(@ModelAttribute PhienBan phienBan, @RequestParam Integer id) {
        Optional<PhienBan> existingPhienBan = Optional.ofNullable(phienBanService.getById(id));
        if (existingPhienBan.isEmpty()) {
            return "redirect:/phienban"; // Redirect if not found
        }
        PhienBan updatedPhienBan = existingPhienBan.get();
        updatedPhienBan.setMaPhienBan(phienBan.getMaPhienBan());
        updatedPhienBan.setTenPhienBan(phienBan.getTenPhienBan());
        updatedPhienBan.setTrangThai(phienBan.getTrangThai());
        phienBanService.save(phienBan);
        return "redirect:/phienban"; // Redirect to the list after updating
    }
}
