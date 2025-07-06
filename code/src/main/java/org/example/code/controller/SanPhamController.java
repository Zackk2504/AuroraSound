package org.example.code.controller;

import org.example.code.DTO.SanPhamChiTietDTO;
import org.example.code.DTO.SanPhamDTO;
import org.example.code.model.SanPham;
import org.example.code.model.SanPhamChiTiet;
import org.example.code.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class SanPhamController {
    @Autowired
    private SanPhamService sanPhamService;
    @Autowired
    private MauSacService mauSacService;
    @Autowired
    private PhienBanService phienBanService;
    @Autowired
    private LoaiSanPhanService loaiSanPhamService;
    @Autowired
    private ThuongHieuService thuongHieuService;
    @Autowired
    private XuatXuService xuatXuService;
    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;


    @GetMapping("/san-pham")
    public String getSanPham(Model model) {
        model.addAttribute("dsSanPham", sanPhamService.getAllSanPhams());
        model.addAttribute("sanPham", new SanPham());
//        model.addAttribute("danhsachmausac",mauSacService.getAll());
//        model.addAttribute("danhsachphienban",phienBanService.getall());
        model.addAttribute("danhsachsanpham",sanPhamService.getAllSanPhams());
        model.addAttribute("dsLoai", loaiSanPhamService.getAllLoaiSanPhams());
        model.addAttribute("dsThuongHieu", thuongHieuService.getAllThuongHieus());
        model.addAttribute("dsXuatXu", xuatXuService.getAll());
        return "admin/sanPham"; // This should match the name of your view template
    }
    @GetMapping("/sanphamct/add")
    public String addSanPham(Model model, @RequestParam(required = false) Integer id) {
        SanPham sanPham = sanPhamService.getSanPhamById(id)
                .orElse(new SanPham());
        List<SanPhamChiTiet> chiTietList = sanPhamChiTietService.getBySanPhamId(id); // Lấy danh sách SPCT

        SanPhamDTO dto = new SanPhamDTO();

            dto.setId(sanPham.getId());
            dto.setTenSanPham(sanPham.getTenSanPham());
            dto.setMoTa(sanPham.getMoTa());
            dto.setTrangThai(sanPham.getTrangThai());
            dto.setIdLoaiSanPham(sanPham.getIdLoaisanpham().getId());
            dto.setIdThuongHieu(sanPham.getIdThuonghieu().getId());
            dto.setIdXuatXu(sanPham.getIdXuatxu().getId());

            // Gán chi tiết vào DTO
            dto.setChiTietList(chiTietList.stream().map(spct -> {
                SanPhamChiTietDTO ct = new SanPhamChiTietDTO();
                ct.setId(spct.getId());
                ct.setIdMausac(spct.getIdMausac().getId());
                ct.setIdPhienban(spct.getIdPhienban().getId());
                ct.setAnhSP(spct.getAnhSP());
                ct.setSoLuongTon(spct.getSoLuongTon());
                ct.setDonGia(spct.getDonGia());
                ct.setMoTa(spct.getMoTa());
                ct.setTrangThai(spct.getTrangThai());
                return ct;
            }).collect(Collectors.toList()));

        model.addAttribute("sanPhamdto", dto);
        model.addAttribute("danhsachmausac", mauSacService.getAll());
        model.addAttribute("danhsachphienban", phienBanService.getall());
        model.addAttribute("danhsachsanpham",sanPhamService.getAllSanPhams());
        model.addAttribute("dsLoai", loaiSanPhamService.getAllLoaiSanPhams());
        model.addAttribute("dsThuongHieu", thuongHieuService.getAllThuongHieus());
        model.addAttribute("dsXuatXu", xuatXuService.getAll());

        return "admin/sanPhamChiTiet"; // This should match the name of your view template for adding SanPhamChiTiet
    }

    @PostMapping("/sanphamct/add")
    public String saveSanPhamct(@ModelAttribute SanPhamDTO dto,Model model) {
        sanPhamService.addSp(dto);
        return "redirect:/admin/san-pham";
//        return ResponseEntity.ok(dto);
    }
    @PostMapping("/sanpham/add")
    public String saveSanPham(@ModelAttribute("SanPham") SanPham sanPham) {
        sanPhamService.themSanPham(sanPham);
        return "redirect:/admin/sanphamct/add?id="+ sanPham.getId(); // Redirect to the list after saving
    }






    @GetMapping("/sanpham/edit/{id}")
    public ResponseEntity<?> editSanPham(Model model,@PathVariable Integer id,
                              @ModelAttribute SanPhamDTO sanPhamDTO) {
        System.out.println("Editing SanPham with ID: " + id);
        SanPham sanPham = sanPhamService.getAllSanPhams().stream()
                .filter(sp -> sp.getId().equals(id))
                .findFirst()
                .orElse(null);
        model.addAttribute("sanpham",sanPham );
       return ResponseEntity.ok(sanPham);
    }

    @PostMapping("/sanpham/edit")
    public ResponseEntity<?> updateSanPham(@ModelAttribute SanPhamDTO dto) {
        sanPhamService.addSp(dto);
        return ResponseEntity.ok(dto); // Return the updated DTO or a success message
    }
    
}
