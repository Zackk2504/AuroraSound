package org.example.code.service;

import org.example.code.DTO.SanPhamBienTheDTO;
import org.example.code.model.SanPham;
import org.example.code.model.SanPhamChiTiet;
import org.example.code.repo.SanPhamChiTietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SanPhamChiTietService {
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;


    public Optional<SanPhamChiTiet> getSanPhamChiTietById(Integer id) {
        return sanPhamChiTietRepository.findById(id);
    }

    public List<SanPhamChiTiet> getAllSanPhamChiTiet() {
        return sanPhamChiTietRepository.findAllByTrangThai("hoat_dong");
    }
    public List<SanPhamChiTiet> getBySanPhamId(Integer sanPhamId) {
        return sanPhamChiTietRepository.findByIdSanpham_Id(sanPhamId);
    }

    public void addAndEdit(SanPhamChiTiet sanPhamChiTiet) {
        sanPhamChiTietRepository.save(sanPhamChiTiet);
    }



    public List<SanPhamChiTiet> locSanPham(Integer idThuongHieu, Integer idMauSac, Integer idPhienBan, Integer idXuatXu,Integer idLoaiSanPham ) {
        return sanPhamChiTietRepository.locSanPham(
                idThuongHieu,
                idMauSac,
                idPhienBan,
                idXuatXu,
                idLoaiSanPham
        );
    }

    public void checksoluong(Integer id){
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(id).orElseThrow();
        if (sanPhamChiTiet.getSoLuongTon() == 0) {
            sanPhamChiTiet.setTrangThai("khong_hoat_dong");
        }else {
            sanPhamChiTiet.setTrangThai("hoat_dong");
        }
        sanPhamChiTietRepository.save(sanPhamChiTiet);
    }

    public Long countBienTheBySanPhamId(Integer idSanPham) {
        return sanPhamChiTietRepository.countBienTheBySanPhamId(idSanPham);
    }

}
