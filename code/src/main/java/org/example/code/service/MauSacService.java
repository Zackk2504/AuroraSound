package org.example.code.service;

import org.example.code.model.MauSac;
import org.example.code.repo.MauSacRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MauSacService {
    @Autowired
    MauSacRepository mauSacRepository;

    public List<MauSac> getAll() {
        return mauSacRepository.findAllByTrangThai(true);
    }

    public Optional<MauSac> getById(Integer id) {
        return mauSacRepository.findById(id);
    }
    public Page<MauSac> getAllpage(Pageable pageable) {
        return mauSacRepository.findAll(pageable);
    }
    public MauSac save(MauSac mauSac) {
        return mauSacRepository.save(mauSac);
    }
}
