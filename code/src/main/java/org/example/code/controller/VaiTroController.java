package org.example.code.controller;

import org.example.code.service.VaiTroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VaiTroController {
    @Autowired
    private VaiTroService vaiTroService;

    @GetMapping("/vai-tro")
    public String getVaiTroPage(Model model) {
        model.addAttribute("vaiTros", vaiTroService.getAllVaiTros());
        return "vai-tro";
    }

}
