package org.example.code.controller;

import org.example.code.model.AnhSanPham;
import org.example.code.model.SanPham;
import org.example.code.service.AccountService;
import org.example.code.service.GioHangService;
import org.example.code.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LoginController {
    private final AccountService accountService;
    @Autowired
    private SanPhamService sanPhamService;

    public LoginController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @GetMapping("/oauth2/success")
    public String handleOAuth2Success(Authentication authentication) {
        accountService.LoginWithGG(authentication);
        System.out.println("OAuth2 login successful: " + authentication.getPrincipal());
        return "redirect:/index"; // Redirect to index page after successful login
    }

    @GetMapping("/logout")
    public String logoutPage() {
        return "index"; // index.html
    }
}
