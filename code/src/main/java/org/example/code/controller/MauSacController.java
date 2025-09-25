package org.example.code.controller;

import org.example.code.model.MauSac;
import org.example.code.service.MauSacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public String getMauSacPage(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "5") int size) {

        Page<MauSac> mauSacPage = mauSacService.getAllpage(PageRequest.of(page, size));

        model.addAttribute("dsMauSac", mauSacPage.getContent());
        model.addAttribute("mauSac", new MauSac());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", mauSacPage.getTotalPages());

        return "admin/macSac"; // sửa lại tên file cho đúng (trước bạn viết nhầm "macSac")
    }


    @PostMapping("/mau-sac/add")
    public String addMau(@ModelAttribute MauSac mauSac) {
        mauSacService.save(mauSac);
        return "redirect:/mau-sac"; // Redirect to the list page after adding
    }

    @GetMapping("/mau-sac/edit/{id}")
    public String editMauSacPage(Model model,@PathVariable Integer id) {
        Optional<MauSac> mauSacOptional = mauSacService.getById(id);
        if (mauSacOptional.isPresent()) {
            model.addAttribute("dsMauSac", mauSacService.getAll());
            model.addAttribute("mauSac", mauSacOptional.get());
        } else {
            model.addAttribute("error", "Mau Sac not found");
        }
        return "admin/macSac"; // Assuming you have a Thymeleaf template named editMauSac.html
    }
    @PostMapping("/mau-sac/edit/{id}")
    public String editMauSac(@ModelAttribute MauSac mauSac, @PathVariable Integer id) {
        Optional<MauSac> existingMauSac = mauSacService.getById(id);
        if (existingMauSac.isEmpty()) {
            return "redirect:/admin/mau-sac";
        }
        MauSac mauSac1 = existingMauSac.get();
        mauSac1.setMaMauSac(mauSac.getMaMauSac());
        mauSac1.setTenMauSac(mauSac.getTenMauSac());
        mauSac1.setTrangThai(mauSac.getTrangThai());
        mauSacService.save(mauSac1);
        return "redirect:/admin/mau-sac";
    }

}
