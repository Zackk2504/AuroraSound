package org.example.code.controller;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class LoaiSanPhanController {
    @GetMapping("/loai-san-phan")
    public String getAllLoaiSanPhan() {
        // Logic to retrieve all Loai San Phan and return a view
        return "admin/"; // This should be the name of your view template
    }
}
