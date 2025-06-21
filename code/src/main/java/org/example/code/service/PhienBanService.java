package org.example.code.service;

import org.example.code.model.PhienBan;
import org.example.code.repo.PhienBanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhienBanService {
    @Autowired
    PhienBanRepository phienBanRepository;

    public List<PhienBan> getall() {
        return phienBanRepository.findAll();
    }

    public PhienBan getById(Integer id) {
        return phienBanRepository.findById(id).orElse(null);
    }

    public PhienBan save(PhienBan phienBan) {
        return phienBanRepository.save(phienBan);
    }
}
