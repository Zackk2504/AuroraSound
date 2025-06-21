package org.example.code.controller;

import org.example.code.model.DiaChi;
import org.example.code.service.DiaChiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class DiaChiController {
    @Autowired
    public DiaChiService diaChiService;

    @GetMapping("/diachi")
    public String getAll(Model model) {
        model.addAttribute("listDiaChi", diaChiService.getAll());
        return "diaChi/diaChi";
    }

    @RequestMapping("/diachi/add")
    public String addOrUpdate(Model model, @ModelAttribute ("DiaChi") DiaChi diaChi) {
        return "diachi-add";
    }

    @PostMapping("/diachi/add")
    public ResponseEntity<Optional> add(@ModelAttribute ("DiaChi") DiaChi diaChi) {
        Optional saved = diaChiService.addOrUpdate(diaChi);
        return ResponseEntity.ok(saved);
    }

    @RequestMapping("/diachi/update?id={id}")
    public String update(Model model, @RequestParam Integer id) {
        Optional<DiaChi> diaChiOptional = diaChiService.diaChiRepository.findById(id);
        if(diaChiOptional.isPresent()) {
            model.addAttribute("DiaChi", diaChiOptional);
        } else {
            System.out.println("dia chi ko hop le");
            return "redirect:/diachi";
        }
        return "redirect:/diachi";
    }

    @PostMapping("/diachi/update?id={id}")
    public String update(@ModelAttribute("DiaChi") DiaChi diaChi,@RequestParam Integer id) {
        Optional<DiaChi> existingDiaChi = diaChiService.findById(id);
        DiaChi diaChi1 = new DiaChi();
        diaChi1 = existingDiaChi.get();
        diaChi1.setQuanHuyenXaThanhPho(diaChi.getQuanHuyenXaThanhPho());
        diaChi1.setSoNha(diaChi.getSoNha());
        diaChiService.addOrUpdate(diaChi1);
        return "redirect:/diachi";
    }


}
