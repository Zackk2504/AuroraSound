package org.example.code.service;

import org.example.code.DTO.KhachHangDTO;
import org.example.code.model.KhachHang;
import org.example.code.repo.KhachHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KhachHangService {
    @Autowired
    private KhachHangRepository khachHangRepository;
    @Autowired
    private DiaChiService diaChiService;

    public Optional<KhachHang> getKhachHangByUsername(String username) {
        return khachHangRepository.findByTenDangNhap(username);

    }

    public Optional<KhachHang> getKhachHangBySDT(String id) {
        return khachHangRepository.findBySoDT(id);
    }
    public void addAndEdit(KhachHang khachHang) {
        khachHangRepository.save(khachHang);
    }
    public Optional<KhachHang> getKhachHangByEmail(String email) {
        return khachHangRepository.findByEmail(email);
    }
    public boolean capNhatThongTinCaNhan(KhachHangDTO dto) {
        Optional<KhachHang> optional = khachHangRepository.findById(dto.getId());
        if (optional.isEmpty()) {
            return false;
        }

        KhachHang kh = optional.get();
        kh.setHoTen(dto.getHoTen());
        kh.setSoDT(dto.getSoDT());
        kh.setNgaySinh(dto.getNgaySinh());

        khachHangRepository.save(kh);
        return true;
    }
}
