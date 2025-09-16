package org.example.code.service;

import org.example.code.model.ThuongHieu;
import org.example.code.repo.ThuongHieuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ThuongHieuService {
    @Autowired
    private ThuongHieuRepository thuongHieuRepository;

    public List<ThuongHieu> getAllThuongHieus() {
        return thuongHieuRepository.findAllByTrangThai(true);
    }

    public Optional<ThuongHieu> getThuongHieuById(Integer id) {
        return thuongHieuRepository.findById(id);
    }

    public ThuongHieu saveThuongHieu(ThuongHieu thuongHieu) {
        return thuongHieuRepository.save(thuongHieu);
    }
}
