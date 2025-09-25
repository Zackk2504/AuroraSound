package org.example.code.controller;

import org.example.code.model.PhienBan;
import org.example.code.service.PhienBanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class PhienBanController {
    @Autowired
    PhienBanService phienBanService;

    @GetMapping("/phien-ban")
    public String list(Model model,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "5") int size) {

        Page<PhienBan> phienBanPage = phienBanService.getAllpage(PageRequest.of(page, size));

        model.addAttribute("list", phienBanPage.getContent());
        model.addAttribute("phienBan", new PhienBan());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", phienBanPage.getTotalPages());

        return "admin/phienBan";
    }

    @GetMapping("/phienban/save")
    public String addPhienBan(Model model) {
        return "add_phienban";
    }

    @PostMapping("/phienban/save")
    public String savePhienBan(@ModelAttribute PhienBan phienBan) {
        phienBanService.save(phienBan);
        return "redirect:/admin/phien-ban"; // Redirect to the list after saving
    }

    @GetMapping("/phienban/edit/{id}")
    public String editPhienBan(Model model,@PathVariable Integer id) {
        PhienBan phienBan = phienBanService.getById(id).orElse(null);
        if (phienBan != null) {
            model.addAttribute("phienBan", phienBan);
            model.addAttribute("list", phienBanService.getall());
            return "admin/phienBan"; // This should correspond to a view template named 'edit_phienban'
        } else {
            return "redirect:/admin/phien-ban"; // Redirect if not found
        }
    }

    @PostMapping("/phienban/edit?id={id}")
    public String updatePhienBan(@ModelAttribute PhienBan phienBan, @RequestParam Integer id) {
        Optional<PhienBan> existingPhienBan = Optional.ofNullable(phienBanService.getById(id).orElse(null));
        if (existingPhienBan.isEmpty()) {
            return "admin/phienBan"; // Redirect if not found
        }
        PhienBan updatedPhienBan = existingPhienBan.get();
        updatedPhienBan.setMaPhienBan(phienBan.getMaPhienBan());
        updatedPhienBan.setTenPhienBan(phienBan.getTenPhienBan());
        updatedPhienBan.setTrangThai(phienBan.getTrangThai());
        phienBanService.save(phienBan);
        return "redirect:/admin/phien-ban"; // Redirect to the list after updating
    }
}
