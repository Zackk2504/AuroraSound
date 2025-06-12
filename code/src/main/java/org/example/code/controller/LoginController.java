package org.example.code.controller;

import org.example.code.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    private final AccountService accountService;

    public LoginController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/index")
    public String indexPage() {
        return "index"; // index.html
    }

    @GetMapping("/oauth2/success")
    public ResponseEntity<?> handleOAuth2Success(Authentication authentication) {
        accountService.LoginWithGG(authentication);
        return ResponseEntity.ok("Đăng nhập thành công!");
    }
}
