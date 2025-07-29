package org.example.code.service;

import org.example.code.model.DiaChi;
import org.example.code.model.KhachHang;
import org.example.code.repo.DiaChiRepository;
import org.example.code.repo.KhachHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiaChiService {
    @Autowired
    public DiaChiRepository diaChiRepository;

    @Autowired
    private KhachHangRepository khachHangRepo;

    public List<DiaChi> getAll() {
        return (diaChiRepository.findAll());
    }

    public Optional<DiaChi> addOrUpdate(DiaChi diaChi) {
        return Optional.of(diaChiRepository.save(diaChi));
    }

    public Optional<DiaChi> findById(Integer id) {
        return diaChiRepository.findById(id);
    }

    public Optional<DiaChi> findByMacdinhTrue() {
        return diaChiRepository.findByMacdinh(true);
    }

    public List<DiaChi> layDanhSachDiaChiTheoKhachHang(Integer khachHangId) {
        return diaChiRepository.findByKhachHang_Id(khachHangId);
    }

    public DiaChi themHoacSuaDiaChi(DiaChi diaChi, Integer khachHangId) {
        KhachHang kh = khachHangRepo.findById(khachHangId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

        diaChi.setKhachHang(kh);
        List<DiaChi> existingAddresses = diaChiRepository.findByKhachHang_Id(khachHangId);
        if (existingAddresses.isEmpty()) {
            diaChi.setMacdinh(true);
        }

        if (Boolean.TRUE.equals(diaChi.getMacdinh())) {
            // Reset tất cả địa chỉ khác về false
            List<DiaChi> diaChiList = diaChiRepository.findByKhachHang_Id(khachHangId);
            for (DiaChi dc : diaChiList) {
                dc.setMacdinh(false);
            }
            diaChiRepository.saveAll(diaChiList);
        }else {
            // Nếu địa chỉ không phải mặc định, thì không cần reset
            diaChi.setMacdinh(false);
            diaChiRepository.save(diaChi);
        }

        return diaChiRepository.save(diaChi);
    }

    public DiaChi layDiaChiTheoId(Integer id) {
        return diaChiRepository.findById(id).orElse(null);
    }
}
