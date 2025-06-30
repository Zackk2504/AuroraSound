package org.example.code.service;

import org.example.code.model.HoaDon;
import org.example.code.repo.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HoaDonService {
    @Autowired
    private HoaDonRepository hoaDonRepository;

    public Optional<HoaDon> getHoaDonById(Integer id) {
        return hoaDonRepository.getHoaDonById(id);
    }

    public void addAndEdit(HoaDon hoaDon) {
        hoaDonRepository.save(hoaDon);
    }

    public void deleteHoaDon(Integer id) {
        hoaDonRepository.deleteById(id);
    }
}
