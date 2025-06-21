package org.example.code.service;

import org.example.code.model.DiaChi;
import org.example.code.repo.DiaChiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiaChiService {
    @Autowired
    public DiaChiRepository diaChiRepository;

    public List<DiaChi> getAll() {
        return (diaChiRepository.findAll());
    }

    public Optional<DiaChi> addOrUpdate(DiaChi diaChi) {
        return Optional.of(diaChiRepository.save(diaChi));
    }

    public Optional<DiaChi> findById(Integer id) {
        return diaChiRepository.findById(id);
    }

}
