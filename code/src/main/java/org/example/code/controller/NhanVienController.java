package org.example.code.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.code.model.DiaChi;
import org.example.code.model.HoaDon;
import org.example.code.model.NhanVien;
import org.example.code.model.VaiTro;
import org.example.code.service.DiaChiService;
import org.example.code.service.HoaDonService;
import org.example.code.service.NhanVienService;
import org.example.code.service.VaiTroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class NhanVienController {
    @Autowired
    private NhanVienService nhanVienService;

    @Autowired
    private VaiTroService vaiTroService;
    @Autowired
    private HoaDonService hoaDonService;
    @Autowired
    private DiaChiService diaChiService;

    @GetMapping("/nhan-vien/login")
    public String login() {
        return "UI/employee/login";
    }

    @GetMapping("/nhan-vien/logout")
    public String logout() {
        return "redirect:/nhan-vien/login"; // Redirect to login page after logout
    }
    @GetMapping("/nhan-vien/home")
    public String home(Model model) {
        model.addAttribute("ListnhanVien", nhanVienService.getAll());
        return "admin/Index"; // Redirect to employee home page
    }

    @PostMapping("/admin/nhan-vien/create")
    public String createNhanVien(Model model, @ModelAttribute ("NhanVien") NhanVien nhanVien) {
        nhanVienService.themNhanVien(nhanVien);
        return "redirect:/admin/nhan-vien"; // Redirect to employee creation page
    }

    @GetMapping("/admin/nhan-vien")
    public String home(HttpServletRequest request, Model model, @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "5") int size) {
        Page<NhanVien> nhanVienPage = nhanVienService.getAll(PageRequest.of(page, size));
        model.addAttribute("nhanVienPage", nhanVienPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", nhanVienPage.getTotalPages());
        model.addAttribute("nhanVien", new NhanVien());
        model.addAttribute("danhSachVaiTro", vaiTroService.getAllVaiTros());
        return "admin/nhanVien";
    }

    @PostMapping("/admin/nhan-vien/save")
    public String updateNhanVien(@ModelAttribute NhanVien nhanVien) {
        Optional<NhanVien> nhanVien1 = nhanVienService.getById(nhanVien.getId());
        VaiTro vaiTro = vaiTroService.getVaiTroByName("employee");
        if (nhanVien1.isPresent()){
            NhanVien existingNhanVien = nhanVien1.get();
            existingNhanVien.setHoTen(nhanVien.getHoTen());
            existingNhanVien.setGioiTinh(nhanVien.getGioiTinh());
            existingNhanVien.setSoDT(nhanVien.getSoDT());
            existingNhanVien.setTrangThai(nhanVien.getTrangThai());
            existingNhanVien.setNgaySinh(nhanVien.getNgaySinh());
            existingNhanVien.setDiaChi(nhanVien.getDiaChi());
            existingNhanVien.setVaiTro(vaiTro);
            nhanVienService.addAndUpdate(existingNhanVien);
            return "redirect:/admin/nhan-vien";
        }
        return "admin/nhanVien";
    }
    @GetMapping("/nhan-vien/thay-doi-trang-thai/{id}")
    public String thayDoiTrangThaiHoaDon(@PathVariable Integer id) {
        Optional<HoaDon> hoaDon = hoaDonService.getHoaDonById(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String tenDangNhap = auth.getName(); // Lấy tên đăng nhập của người dùng hiện tại
        NhanVien nhanVien = nhanVienService.getNhanVienByTenDangNhap(tenDangNhap);
        if (nhanVien == null) {
            throw new RuntimeException("Không tìm thấy nhân viên với tên đăng nhập: " + tenDangNhap);
        }
        if (hoaDon.isPresent()) {
            HoaDon hd = hoaDon.get();
            String trangThai = hd.getTrangThaiHoaDon();

            switch (trangThai) {
                case "DA_XAC_NHAN":
                    hd.setTrangThaiHoaDon("DA_GIAO_HANG");
                    break;
                case "DA_GIAO_HANG":
                    hd.setTrangThaiHoaDon("THANH_CONG");
                    break;
                case "THANH_CONG":
                    hd.setTrangThaiHoaDon("THAT_BAI");
                    break;
                // Nếu cần, có thể thêm case nữa cho vòng lặp trạng thái
                default:
                    break; // Không thay đổi nếu không khớp trạng thái
            }
            hd.setIdNhanvien(nhanVien); // Cập nhật nhân viên xử lý hóa đơn
            hoaDonService.addAndEdit(hd);
        }
        return "redirect:/ban-hang-tai-quay/hoa-don/chi-tiet/" + id; // Redirect về trang chi tiết hóa đơn
    }

    @GetMapping("/nhan-vien/xac-nhan-don-hang/{id}")
    public String getHoaDonPage(Model model,@PathVariable Integer id,
                                @RequestParam(required = false) String ghiChu,
                                @RequestParam(required = false) String diaChimoi,
                                @RequestParam(required = false) String tienTraSau) {
        Optional<HoaDon> hoaDon = hoaDonService.getHoaDonById(id);
        if (hoaDon.isEmpty()) {
            throw new RuntimeException("Không tìm thấy hóa đơn với ID: " + id);
        }
        System.out.println("Ghi chú: " + ghiChu);
        System.out.println("Dia chỉ mới: " + diaChimoi);
        System.out.println("Tien trả sau: " + tienTraSau);
        hoaDonService.xacNhanDonHang(hoaDon.get().getId(), ghiChu, diaChimoi, tienTraSau);

        return "redirect:/ban-hang-tai-quay/hoa-don/chi-tiet/" + id; // Trang hiển thị danh sách hóa đơn
    }
    @GetMapping("/nhan-vien/huy-don-hang/{id}")
    public String huyDonHang(@PathVariable Integer id) {

        hoaDonService.huyDonHang(id);
        return "redirect:/ban-hang-tai-quay/hoa-don/chi-tiet/" + id; // Redirect về trang chi tiết hóa đơn
    }
    @GetMapping("/nhan-vien/list-dia-chi-khach")
    @ResponseBody
    public ResponseEntity<List<DiaChi>> getListDiaChiKhach(@RequestParam Integer idhd) {
        HoaDon hoaDon = hoaDonService.getHoaDonById(idhd)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với ID: " + idhd));
        if (hoaDon.getIdKhachhang() != null) {
            List<DiaChi> diaChiList = diaChiService.layDanhSachDiaChiTheoKhachHang(hoaDon.getIdKhachhang().getId());
            System.out.println("List Dia Chi: " + diaChiList);
            return ResponseEntity.ok(diaChiList); // Trả về JSON
        }
        System.out.println("hello" + idhd);
        return ResponseEntity.ok(Collections.emptyList());
    }



}
