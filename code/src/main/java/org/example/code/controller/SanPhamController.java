package org.example.code.controller;

import org.example.code.DTO.SanPhamBienTheDTO;
import org.example.code.DTO.SanPhamChiTietDTO;
import org.example.code.DTO.SanPhamDTO;
import org.example.code.model.AnhSanPham;
import org.example.code.model.SanPham;
import org.example.code.model.SanPhamChiTiet;
import org.example.code.repo.AnhSanPhamRepository;
import org.example.code.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
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
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private AnhSanPhamService AnhsanPhamService;


    @GetMapping("/san-pham")
    public String getSanPham(   @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "tensp", required = false) String tensp,
                                @RequestParam(value = "loaiId", required = false) Integer loaiId,
                                @RequestParam(value = "thuongHieuId", required = false) Integer thuongHieuId,
                                @RequestParam(value = "xuatXuId", required = false) Integer xuatXuId,
                                @RequestParam(value = "trangThai", required = false) String trangThai,
                                Model model) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by("id").descending());
        if (trangThai != null && trangThai.trim().isEmpty()) {
            trangThai = null;
        }
        Page<SanPham> dsSanPham = sanPhamService.findByFilter(tensp,loaiId, thuongHieuId, xuatXuId, trangThai, pageable);

        model.addAttribute("dsSanPham", dsSanPham);
        model.addAttribute("sanPham", new SanPham());

//        model.addAttribute("danhsachmausac",mauSacService.getAll());
//        model.addAttribute("danhsachphienban",phienBanService.getall());
        List<SanPhamBienTheDTO> bientheList = sanPhamService.countBienTheTheoSanPham();
        for (SanPhamBienTheDTO dto : bientheList) {
            System.out.println(dto + "dto");
        }
        model.addAttribute("listBienThe", bientheList);
//        model.addAttribute("danhsachsanpham",sanPhamService.getAllSanPhams());
        model.addAttribute("dsLoai", loaiSanPhamService.getAllLoaiSanPhams());
        model.addAttribute("dsThuongHieu", thuongHieuService.getAllThuongHieus());
        model.addAttribute("dsXuatXu", xuatXuService.getAllxx());
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
        model.addAttribute("listChiTiet", chiTietList);
        model.addAttribute("danhsachmausac", mauSacService.getAll());
        model.addAttribute("danhsachphienban", phienBanService.getall());
        model.addAttribute("danhsachsanpham",sanPhamService.getAllSanPhams());
        model.addAttribute("dsLoai", loaiSanPhamService.getAllLoaiSanPhams());
        model.addAttribute("dsThuongHieu", thuongHieuService.getAllThuongHieus());
        model.addAttribute("dsXuatXu", xuatXuService.getAllxx());

        return "admin/sanPhamChiTiet"; // This should match the name of your view template for adding SanPhamChiTiet
    }

    @PostMapping("/sanphamct/add")
    public String saveSanPhamct(@ModelAttribute SanPhamDTO dto,
                                @RequestParam(value = "productImages", required = false) MultipartFile[] productImages,
                                RedirectAttributes redirectAttributes) {
        System.out.println("Saving SanPham with ID: " + dto.getId());
        sanPhamService.addSp(dto);
        if (productImages != null) {
            System.out.println("co anh m oi");
            for (MultipartFile image : productImages) {
                if (!image.isEmpty()) {
                    try {
                        String imageUrl = cloudinaryService.uploadImage(image);

                        AnhSanPham anh = new AnhSanPham();
                        anh.setUrl(imageUrl);
                        SanPham sanPham = sanPhamService.getSanPhamById(dto.getId())
                                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
                        anh.setIdSanpham(sanPham); // QUAN TRỌNG: Liên kết với sản phẩm
                        AnhsanPhamService.save(anh);

                        System.out.println("hello: " + imageUrl); // Log để debug
                    } catch (IOException e) {
                        System.err.println("Lỗi upload ảnh: " + e.getMessage());
                        redirectAttributes.addFlashAttribute("error", "Lỗi khi upload ảnh: " + e.getMessage());
                    }
                }
            }
        }else {
            System.out.println("khong co anh m oi");
        }

        redirectAttributes.addFlashAttribute("success", "Thêm sản phẩm thành công!");
        return "redirect:/admin/san-pham";

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

    @GetMapping("/sanpham/{id}/images")
    @ResponseBody
    public List<String> getProductImages(@PathVariable Integer id) {
        return AnhsanPhamService.getAllListByIDSP(id)
                .stream()
                .map(AnhSanPham::getUrl)
                .limit(3) // Chỉ lấy tối đa 3 ảnh
                .collect(Collectors.toList());
    }

    @GetMapping("/sanpham/dunghoatdong/{id}")
    public String dunghd(@PathVariable Integer id) {
        sanPhamService.dungHd(id);
        return "redirect:/admin/san-pham";
    }
    @GetMapping("/sanpham/tieptuchd/{id}")
    public String tieptuchd(@PathVariable Integer id) {
        sanPhamService.tieptuchd(id);
        return "redirect:/admin/san-pham";
    }
}
