package org.example.code.service;

import org.example.code.model.KhachHang;
import org.example.code.repo.KhachHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KhachHangService {
    @Autowired
    private KhachHangRepository khachHangRepository;

    public Optional<KhachHang> getKhachHangByUsername(String username) {
        return khachHangRepository.findByTenDangNhap(username);

    }

    public Optional<KhachHang> getKhachHangBySDT(String id) {
        return khachHangRepository.findBySoDT(id);
    }
    public void addAndEdit(KhachHang khachHang) {
        khachHangRepository.save(khachHang);
    }
}
