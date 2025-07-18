package org.example.code.service;

import org.example.code.model.*;
import org.example.code.repo.GioHangChiTietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GioHangChiTietService {
    @Autowired
    private GioHangChiTietRepository gioHangChiTietRepository;

    @Autowired
    private GioHangService gioHangService;

    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;

    @Autowired
    private KhachHangService khachHangService;

    public List<GioHangChiTiet> findByIds(List<Integer> ids) {
        return gioHangChiTietRepository.findAllById(ids);
    }

    public GioHangChiTiet getGioHangChiTietById(Integer id) {
        return gioHangChiTietRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gio hang chi tiet not found with id: " + id));
    }


    public void addGioHangChiTiet(Integer sanPhamCTId, Integer soLuong) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<KhachHang> kh = khachHangService.getKhachHangByUsername(username);
        if (!kh.isPresent()) {
            throw new RuntimeException("Khach hang not found");
        }
        KhachHang khachHang = kh.get();
        GioHangChiTiet gioHangChiTiet;

        GioHang gioHang = gioHangService.getGioHangByKhachHang(khachHang.getId());
        if (gioHang == null) {
            gioHang = new GioHang();
            gioHang.setIdKhachhang(khachHang);
            gioHangService.add(gioHang);
        }
        Optional<GioHangChiTiet> gioHangChiTietOpt = gioHangChiTietRepository.findByIdGiohang_IdAndIdSanphamchitiet_Id(gioHang.getId(), sanPhamCTId);
        if (!gioHangChiTietOpt.isPresent()) {
            gioHangChiTiet = new GioHangChiTiet();
            Optional<SanPhamChiTiet> sanPhamChiTiet = sanPhamChiTietService.getSanPhamChiTietById(sanPhamCTId);
            if (!sanPhamChiTiet.isPresent()) {
                throw new RuntimeException("San pham chi tiet not found");
            }
            gioHangChiTiet.setIdGiohang(gioHang);
            gioHangChiTiet.setIdSanphamchitiet(sanPhamChiTiet.get());
            gioHangChiTiet.setSoLuong(soLuong);
            gioHangChiTietRepository.save(gioHangChiTiet);
        }else {
            GioHangChiTiet giohangchitiet = gioHangChiTietOpt.get();
            giohangchitiet.setSoLuong(giohangchitiet.getSoLuong() + soLuong);
            gioHangChiTietRepository.save(giohangchitiet);
        }
    }
    public List<GioHangChiTiet> getGioHangChiTietByGioHang() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<KhachHang> kh = khachHangService.getKhachHangByUsername(username);
        if (!kh.isPresent()) {
            throw new RuntimeException("Khach hang not found");
        }
        GioHang gioHang = gioHangService.getGioHangByKhachHang(kh.get().getId());
        if (gioHang == null) {
            throw new RuntimeException("Gio hang not found for khach hang");
        }
        return gioHangChiTietRepository.findByIdGiohang_Id(gioHang.getId());
    }
    public void thaydoisoluongGioHangChiTiet(Integer sanPhamCTId,int soLuong) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        KhachHang khachHang = khachHangService.getKhachHangByUsername(username)
                .orElseThrow(() -> new RuntimeException("Khach hang not found"));

        GioHang gioHang = gioHangService.getGioHangByKhachHang(khachHang.getId());
        if (gioHang == null) {
            throw new RuntimeException("Gio hang not found");
        }

        Optional<GioHangChiTiet> gioHangChiTietOpt = gioHangChiTietRepository
                .findByIdGiohang_IdAndIdSanphamchitiet_Id(gioHang.getId(), sanPhamCTId);

        if (!gioHangChiTietOpt.isPresent()) {
            throw new RuntimeException("San pham chi tiet khong co trong gio hang");
        }

        GioHangChiTiet gioHangChiTiet = gioHangChiTietOpt.get();
        int soLuongHienTai = gioHangChiTiet.getSoLuong();

        if (soLuongHienTai == 1 && soLuong == -1) {
            gioHangChiTietRepository.deleteById(sanPhamCTId);
        } else {
            gioHangChiTiet.setSoLuong(soLuongHienTai + soLuong);
            gioHangChiTietRepository.save(gioHangChiTiet);
        }
    }

    public void xoaGioHangChiTiet(Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<KhachHang> kh = khachHangService.getKhachHangByUsername(username);
        if (!kh.isPresent()) {
            throw new RuntimeException("Khach hang not found");
        }
        GioHang gioHang = gioHangService.getGioHangByKhachHang(kh.get().getId());
        if (gioHang == null) {
            throw new RuntimeException("Gio hang not found for khach hang");
        }
        gioHangChiTietRepository.deleteById(id);
    }

}
