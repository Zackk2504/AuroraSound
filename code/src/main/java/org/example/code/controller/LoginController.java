package org.example.code.controller;

import org.example.code.service.AccountService;
import org.example.code.service.GioHangService;
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

@Controller
public class LoginController {
    private final AccountService accountService;
    @Autowired
    private GioHangService gioHangService;

    public LoginController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/index")
    public String indexPage(Model model) {
        return "index"; // index.html
    }

    @GetMapping("/oauth2/success")
    public ResponseEntity<?> handleOAuth2Success(Authentication authentication) {
        accountService.LoginWithGG(authentication);
        return ResponseEntity.ok("Đăng nhập thành công!");
    }

    @GetMapping("/logout")
    public String logoutPage() {
        return "index"; // index.html
    }
}
