package org.example.code.service;

import org.example.code.model.KhachHang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Map;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private GioHangService gioHangService;
    @Autowired
    private KhachHangService khachHangService;

    @ModelAttribute("slGioHang")
    public Long getSoLuongGioHang() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        KhachHang khachHang;


            if (auth.getPrincipal() instanceof DefaultOidcUser user) {
                String email = user.getAttribute("email");
                khachHang = khachHangService.getKhachHangByEmail(email).orElse(null);
            } else {
                String tenDangNhap = auth.getName();
                khachHang = khachHangService.getKhachHangByUsername(tenDangNhap).orElse(null);
            }

            if (khachHang != null) {
                return gioHangService.demTongSanPhamTrongGio(khachHang.getEmail());
            }


        return 0L;
    }


}

