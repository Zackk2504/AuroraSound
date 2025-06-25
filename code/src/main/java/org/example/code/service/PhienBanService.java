package org.example.code.service;

import org.example.code.model.PhienBan;
import org.example.code.repo.PhienBanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PhienBanService {
    @Autowired
    PhienBanRepository phienBanRepository;

    public List<PhienBan> getall() {
        return phienBanRepository.findAll();
    }

    public Optional<PhienBan> getById(Integer id) {
        return phienBanRepository.findById(id);
    }

    public PhienBan save(PhienBan phienBan) {
        return phienBanRepository.save(phienBan);
    }
}
