package org.example.code.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.code.DTO.DiaChiResponse;
import org.example.code.model.DiaChi;
import org.example.code.model.KhachHang;
import org.example.code.service.DiaChiService;
import org.example.code.service.GhnService;
import org.example.code.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/khach-hang")
public class DiaChiController {
    @Autowired
    public DiaChiService diaChiService;
    @Autowired
    private KhachHangService khachHangService;
    @Autowired
    private GhnService ghnService;

    @GetMapping("/diachi")
    public String getAll(Model model) {
        model.addAttribute("listDiaChi", diaChiService.getAll());
        model.addAttribute("diaChiMoi", new DiaChi());
        return "diaChi/diaChi";
    }

    @RequestMapping("/diachi/save")
    public String addOrUpdate(Model model, @ModelAttribute ("DiaChi") DiaChi diaChi) {
        return "diachi-add";
    }

    @PostMapping("/diachi/save")
    public String luuDiaChi(@ModelAttribute("diaChiMoi") DiaChi diaChi,@RequestParam("diaChifull") String diaChifull) {
        Integer khachHangId;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        KhachHang khachHang;
        if (auth.getPrincipal() instanceof DefaultOidcUser user) {
            String email = user.getAttribute("email");
            khachHang = khachHangService.getKhachHangByEmail(email).get();
            khachHangId = khachHang.getId();
        }else {
            String tenDangNhap = auth.getName();
            khachHang = khachHangService.getKhachHangByUsername(tenDangNhap).orElse(new KhachHang());
            khachHangId = khachHang.getId();
        }
        diaChi.setDiaChi(diaChifull);

        diaChiService.themHoacSuaDiaChi(diaChi, khachHangId);
        return "redirect:/khach-hang/thong-tin";
    }

    @PostMapping("/diachi/save2")
    @ResponseBody
    public ResponseEntity<?> luuDiaChi2(@ModelAttribute DiaChi diaChi,
                                        @RequestParam("diaChifull") String diaChifull,
                                        HttpServletRequest request) {
        try {
            Integer khachHangId;
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            KhachHang khachHang;

            if (auth.getPrincipal() instanceof DefaultOidcUser user) {
                String email = user.getAttribute("email");
                khachHang = khachHangService.getKhachHangByEmail(email).get();
                khachHangId = khachHang.getId();
            } else {
                String tenDangNhap = auth.getName();
                khachHang = khachHangService.getKhachHangByUsername(tenDangNhap).orElse(new KhachHang());
                khachHangId = khachHang.getId();
            }

            diaChi.setDiaChi(diaChifull);
            diaChiService.themHoacSuaDiaChi(diaChi, khachHangId);

            // Trả về thông tin địa chỉ mới nếu cần
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Thêm địa chỉ thành công",
                    "newAddress", diaChi.getDiaChi(),
                    "districtId", diaChi.getDistrictId(),
                    "wardCode", diaChi.getWardCode()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "Có lỗi xảy ra"));
        }
    }

    @RequestMapping("/diachi/edit/{id}")
    public String chinhSuaDiaChi(@PathVariable Integer id, Model model) {
        Integer khachHangId;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        KhachHang khachHang;
        if (auth.getPrincipal() instanceof DefaultOidcUser user) {
            String email = user.getAttribute("email");
            khachHang = khachHangService.getKhachHangByEmail(email).get();
            khachHangId = khachHang.getId();
        }else {
            String tenDangNhap = auth.getName();
            khachHang = khachHangService.getKhachHangByUsername(tenDangNhap).orElse(new KhachHang());
            khachHangId = khachHang.getId();
        }
        DiaChi dc = diaChiService.layDiaChiTheoId(id);
        List<DiaChi> danhSach = diaChiService.layDanhSachDiaChiTheoKhachHang(khachHangId);
        model.addAttribute("danhSachDiaChi", danhSach);
        model.addAttribute("diaChiMoi", dc);
        return "diachi/index";
    }
    @GetMapping("/diachi/{id}")
    @ResponseBody
    public DiaChi getDiaChi(@PathVariable Integer id) {
        return diaChiService.findById(id).orElse(null);
    }


}
