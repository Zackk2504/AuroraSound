package org.example.code.service;

import org.example.code.repo.GioHangChiTietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class GioHangChiTietService {
    @Autowired
    private GioHangChiTietRepository gioHangChiTietRepository;

    public void addGioHangChiTiet(Integer gioHangId, Integer sanPhamId, Integer soLuong) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.out.println("Username: " + username);
    }
}
