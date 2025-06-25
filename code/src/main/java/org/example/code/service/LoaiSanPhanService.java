package org.example.code.service;

import org.example.code.model.LoaiSanPham;
import org.example.code.repo.LoaiSanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoaiSanPhanService {
    @Autowired
    private LoaiSanPhamRepository loaiSanPhanRepository;

    public Optional<LoaiSanPham> getLoaiSanPhamById(Integer id) {
        return loaiSanPhanRepository.findById(id)
                ;
    }

    public LoaiSanPham saveLoaiSanPham(LoaiSanPham loaiSanPham) {
        return loaiSanPhanRepository.save(loaiSanPham);
    }

    public List<LoaiSanPham> getAllLoaiSanPhams() {
        return loaiSanPhanRepository.findAll();
    }
}
