package org.example.code.service;

import org.example.code.model.HoaDonChiTiet;
import org.example.code.repo.HoaDonChiTietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HoaDonChiTietService {
    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    public List<HoaDonChiTiet> getAllHoaDonChiTiet() {
        return hoaDonChiTietRepository.findAll();
    }

    public List<HoaDonChiTiet> getListHoaDonChiTietByIdHoaDon(Integer idHoaDon) {
        return hoaDonChiTietRepository.findAllByIdHoadon_Id(idHoaDon);
    }

    public void addAndEdit(HoaDonChiTiet hoaDonChiTiet) {
        hoaDonChiTietRepository.save(hoaDonChiTiet);
    }

    public Optional<HoaDonChiTiet> getHoaDonChiTietByIdSanPhamAndIdHoaDon(Integer idSanPhamChiTiet, Integer idHoaDon) {
        return hoaDonChiTietRepository.findByIdSanphamchitiet_IdAndIdHoadon_Id(idSanPhamChiTiet, idHoaDon);
    }

    public void delete(Integer id) {
        hoaDonChiTietRepository.deleteById(id);
    }

    public void deleteAllByIdHoaDon(Integer idHoaDon) {
        hoaDonChiTietRepository.deleteAllByIdHoadon_Id(idHoaDon);
    }
}
