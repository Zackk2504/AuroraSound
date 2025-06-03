package org.example.code.controller;

import org.example.code.DTO.DangKiDTO;
import org.example.code.model.Taikhoan;
import org.example.code.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CreateAccountController {
    @Autowired
    AccountService accountService;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("Taikhoan",new Taikhoan());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("Taikhoan") Taikhoan taikhoan) {
        DangKiDTO dto =new DangKiDTO();
        dto.setPass(taikhoan.getPass());
        dto.setEmail(taikhoan.getEmail());
        dto.setUserName(taikhoan.getUsername());

        accountService.taoVaGuiMaDangKi(dto);
        return "verify";
    }
    @PostMapping("/verify")
    public String xacMinh(@RequestParam String maXacNhan) {

        boolean x = accountService.XacMinhMa(maXacNhan);

        if (x){
            System.out.println("thanh cong");
            return "redirect:/login";
        }else {
            System.out.println("ko thanh cong");
            return "verify";
        }
        }


}
