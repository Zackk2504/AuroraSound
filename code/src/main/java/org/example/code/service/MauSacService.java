package org.example.code.service;

import org.example.code.model.MauSac;
import org.example.code.repo.MauSacRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MauSacService {
    @Autowired
    MauSacRepository mauSacRepository;

    public List<MauSac> getAll() {
        return mauSacRepository.findAll();
    }

    public Optional<MauSac> getById(Integer id) {
        return mauSacRepository.findById(id);
    }

    public MauSac save(MauSac mauSac) {
        return mauSacRepository.save(mauSac);
    }
}
