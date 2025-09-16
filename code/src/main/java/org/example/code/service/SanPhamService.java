package org.example.code.service;

import jakarta.transaction.Transactional;
import javassist.NotFoundException;
import org.example.code.DTO.SanPhamBienTheDTO;
import org.example.code.DTO.SanPhamChiTietDTO;
import org.example.code.DTO.SanPhamDTO;
import org.example.code.model.*;
import org.example.code.repo.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SanPhamService {
    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;

    @Autowired
    private MauSacService mauSacService;

    @Autowired
    private LoaiSanPhanService loaiSanPhamService;

    @Autowired
    private PhienBanService phienBanService;

    @Autowired
    private ThuongHieuService thuongHieuService;

    @Autowired
    private XuatXuService xuatXuService;

    public List<SanPham> locSanPham(Integer loaiId, Integer thuongHieuId, Integer xuatXuId) {
        return sanPhamRepository.locSanPham(loaiId, thuongHieuId, xuatXuId);
    }

    public Page<SanPham> findByFilter(Integer loaiId, Integer thuongHieuId, Integer xuatXuId, String trangThai, Pageable pageable) {
        return sanPhamRepository.search(
                loaiId, thuongHieuId, xuatXuId, trangThai, pageable);
    }

    public List<SanPham> getAllSanPhams() {
        return sanPhamRepository.findAllByTrangThai("hoat_dong");
    }

    public Optional<SanPham> getSanPhamById(Integer id) {
        return sanPhamRepository.findById(id);
    }

    public SanPham themSanPham(SanPham sanPham) {
        return sanPhamRepository.save(sanPham);
    }

    public ResponseEntity<?> addSp(SanPhamDTO dto) {
        SanPham sp = new SanPham();
        List<SanPhamChiTiet> chiTietCu = new ArrayList<>();

        if (dto.getId() != null) {
            sp = sanPhamRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
            chiTietCu = sp.getSanPhamChiTietList() != null ? sp.getSanPhamChiTietList() : new ArrayList<>();
        }

        ThuongHieu thuongHieu = thuongHieuService.getThuongHieuById(dto.getIdThuongHieu())
                .orElseThrow(() -> new RuntimeException("Thương hiệu không tồn tại"));
        LoaiSanPham loaiSanPham = loaiSanPhamService.getLoaiSanPhamById(dto.getIdLoaiSanPham())
                .orElseThrow(() -> new RuntimeException("Loại sản phẩm không tồn tại"));
        XuatXu xuatXu = xuatXuService.findByID(dto.getIdXuatXu())
                .orElseThrow(() -> new RuntimeException("Xuất xứ không tồn tại"));

        sp.setTenSanPham(dto.getTenSanPham());
        sp.setMoTa(dto.getMoTa());
        sp.setTrangThai(dto.getTrangThai());
        sp.setIdLoaisanpham(loaiSanPham);
        sp.setIdThuonghieu(thuongHieu);
        sp.setIdXuatxu(xuatXu);

        List<SanPhamChiTiet> chiTietMoi = new ArrayList<>();

        for (SanPhamChiTietDTO ctdto : dto.getChiTietList()) {
            MauSac mauSac = mauSacService.getById(ctdto.getIdMausac())
                    .orElseThrow(() -> new RuntimeException("Màu sắc không tồn tại"));
            PhienBan phienBan = phienBanService.getById(ctdto.getIdPhienban())
                    .orElseThrow(() -> new RuntimeException("Phiên bản không tồn tại"));

            SanPhamChiTiet ct;

            if (ctdto.getId() != null) {
                System.out.println("id chi tiết: " + ctdto.getId());
                // tìm trong chi tiết cũ
                ct = sanPhamChiTietService.getSanPhamChiTietById(ctdto.getId())
                        .orElseThrow(() -> new RuntimeException("Chi tiết sản phẩm không tồn tại"));
            } else {
                ct = new SanPhamChiTiet();
                ct.setIdSanpham(sp);
            }

            ct.setIdSanpham(sp);
            ct.setIdMausac(mauSac);
            ct.setIdPhienban(phienBan);
            ct.setAnhSP(ctdto.getAnhSP());
            ct.setSoLuongTon(ctdto.getSoLuongTon());
            ct.setDonGia(ctdto.getDonGia());
            ct.setMoTa(ctdto.getMoTa());
            ct.setTrangThai(ctdto.getTrangThai());

            chiTietMoi.add(ct);
        }


        List<Integer> idsFromFE = dto.getChiTietList().stream()
                .map(SanPhamChiTietDTO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        for (SanPhamChiTiet old : chiTietCu) {
            if (!idsFromFE.contains(old.getId())) {
                chiTietMoi.add(old); // giữ nguyên bản cũ chưa bị sửa
            }
        }

        sp.setSanPhamChiTietList(chiTietMoi);

        sanPhamRepository.save(sp);
        return ResponseEntity.ok(sp);
    }

    public List<SanPhamBienTheDTO> countBienTheTheoSanPham() {
        List<SanPhamBienTheDTO> result = new ArrayList<>();
        List<SanPham> optionalSanPham = sanPhamRepository.findAll();
        for (SanPham sanPham : optionalSanPham) {
            Long soLuongBienThe = sanPhamChiTietService.countBienTheBySanPhamId(sanPham.getId());
            result.add(new SanPhamBienTheDTO(sanPham.getId(), soLuongBienThe));
        }
        return result;
    }
    public List<SanPham> randomSanPham() {

            List<SanPham> allActive = sanPhamRepository.findAllByTrangThai("hoat_dong");
            Collections.shuffle(allActive);

            // Lấy 4 sản phẩm đầu tiên sau khi xáo trộn
            return allActive.stream()
                    .limit(4)
                    .collect(Collectors.toList());

    }


    @Transactional
    public void dungHd(Integer id) {
        SanPham sp = sanPhamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với id: " + id));

        sp.setTrangThai("khong_hoat_dong");
        sanPhamRepository.save(sp);

        List<SanPhamChiTiet> sanPhamChiTietList = sanPhamChiTietService.getBySanPhamId(id);
        for (SanPhamChiTiet old : sanPhamChiTietList) {
            old.setTrangThai("khong_hoat_dong");
            sanPhamChiTietService.addAndEdit(old); // update lại
        }
    }
    @Transactional
    public void tieptuchd(Integer id) {
        SanPham sp = sanPhamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với id: " + id));

        sp.setTrangThai("hoat_dong");
        sanPhamRepository.save(sp);

        List<SanPhamChiTiet> sanPhamChiTietList = sanPhamChiTietService.getBySanPhamId(id);
        for (SanPhamChiTiet old : sanPhamChiTietList) {
            old.setTrangThai("hoat_dong");
            if (old.getSoLuongTon()==0){
                old.setTrangThai("khong_hoat_dong");
            }
            sanPhamChiTietService.addAndEdit(old); // update lại
        }
    }
}
