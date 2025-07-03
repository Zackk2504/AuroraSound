package org.example.code.controller;

import org.example.code.model.MauSac;
import org.example.code.service.MauSacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class MauSacController {
    @Autowired
    private MauSacService mauSacService;

    @GetMapping("/mau-sac")
    public String getMauSacPage(Model model) {
        model.addAttribute("mauSacList", mauSacService.getAll());
        return "admin/macSac"; // Assuming you have a Thymeleaf template named mauSac.html
    }

    @GetMapping("/mau-sac/add")
    public String addMauSacPage() {
        return "addMauSac"; // Assuming you have a Thymeleaf template named addMauSac.html
    }

    @PostMapping("/mau-sac/add")
    public String addMau(@ModelAttribute MauSac mauSac) {
        mauSacService.save(mauSac);
        return "redirect:/mau-sac"; // Redirect to the list page after adding
    }

    @GetMapping("/mau-sac/edit?id={id}")
    public String editMauSacPage(Model model,@RequestParam Integer id) {
        Optional<MauSac> mauSacOptional = mauSacService.getById(id);
        if (mauSacOptional.isPresent()) {
            model.addAttribute("mauSac", mauSacOptional.get());
        } else {
            model.addAttribute("error", "Mau Sac not found");
        }
        return "editMauSac"; // Assuming you have a Thymeleaf template named editMauSac.html
    }
    @PostMapping("/mau-sac/edit?id={id}")
    public String editMauSac(@ModelAttribute MauSac mauSac,@RequestParam Integer id) {
        Optional<MauSac> existingMauSac = mauSacService.getById(id);
        if (existingMauSac.isEmpty()) {
            return "redirect:/mau-sac"; // Redirect if Mau Sac not found
        }
        MauSac mauSac1 = existingMauSac.get();
        mauSac1.setMaMauSac(mauSac.getMaMauSac());
        mauSac1.setTenMauSac(mauSac.getTenMauSac());
        mauSac1.setTrangThai(mauSac.getTrangThai());
        mauSacService.save(mauSac1);
        return "redirect:/mau-sac"; // Redirect to the list page after editing
    }
}
