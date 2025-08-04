package org.example.code.service;

import org.example.code.model.GioHang;
import org.example.code.repo.GioHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GioHangService {
    @Autowired
    private GioHangRepository gioHangRepository;

    public GioHang add(GioHang gioHang) {
        return gioHangRepository.save(gioHang);
    }

    public GioHang getGioHangByKhachHang(Integer khachHangId) {
        return gioHangRepository.findByIdKhachhang_Id(khachHangId);
    }
    public long demTongSanPhamTrongGio(String email) {
        return gioHangRepository
                .demSoSanPhamTheoTenDangNhap(email)
                ;
    }
}
