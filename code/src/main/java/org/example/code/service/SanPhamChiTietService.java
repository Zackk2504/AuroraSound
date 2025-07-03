package org.example.code.service;

import org.example.code.model.SanPhamChiTiet;
import org.example.code.repo.SanPhamChiTietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SanPhamChiTietService {
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    public Optional<SanPhamChiTiet> getSanPhamChiTietById(Integer id) {
        return sanPhamChiTietRepository.findById(id);
    }

    public List<SanPhamChiTiet> getAllSanPhamChiTiet() {
        return sanPhamChiTietRepository.findAllByTrangThai("hoat_dong");
    }
    public List<SanPhamChiTiet> getBySanPhamId(Integer sanPhamId) {
        return sanPhamChiTietRepository.findByIdSanpham_Id(sanPhamId);
    }

    public void addAndEdit(SanPhamChiTiet sanPhamChiTiet) {
        sanPhamChiTietRepository.save(sanPhamChiTiet);
    }
}
