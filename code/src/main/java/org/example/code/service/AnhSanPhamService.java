package org.example.code.service;

import org.example.code.model.AnhSanPham;
import org.example.code.repo.AnhSanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnhSanPhamService {
    @Autowired
    private AnhSanPhamRepository anhSanPhamRepository;
    public List<AnhSanPham> getAllList() {
        return anhSanPhamRepository.findAll();
    }

    public List<AnhSanPham> getAllListByIDSP(Integer idSanPham) {
        return anhSanPhamRepository.findByIdSanpham_IdOrderByIdDesc(idSanPham);
    }
    public AnhSanPham save(AnhSanPham anhSanPham) {
        return anhSanPhamRepository.save(anhSanPham);
    }
}
