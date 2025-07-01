package org.example.code.controller;

import org.example.code.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class NhanVienController {
    @Autowired
    private NhanVienService nhanVienService;

    @GetMapping("/nhan-vien/login")
    public String login() {
        return "UI/employee/login";
    }
}
