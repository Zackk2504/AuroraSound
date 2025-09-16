package org.example.code.service;

import org.example.code.model.LoaiSanPham;
import org.example.code.repo.LoaiSanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        return loaiSanPhanRepository.findAllByTrangThai(true);
    }
    public List<LoaiSanPham> getAllLoaiSanPham() {
        return loaiSanPhanRepository.findAll();
    }
    public Page<LoaiSanPham> getAllPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return loaiSanPhanRepository.findAll(pageable);
    }
}
