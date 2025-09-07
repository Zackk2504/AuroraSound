package org.example.code.controller;

import org.example.code.model.HoaDon;
import org.example.code.model.LichSuHoaDon;
import org.example.code.model.NhanVien;
import org.example.code.service.HoaDonService;
import org.example.code.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class LichSuHoaDonController {
    @Autowired
    private HoaDonService hoaDonService;
    @Autowired
    private NhanVienService nhanVienService;

    @GetMapping("/admin/lich-su-hoa-don")
    public String lichsuhoadon(  @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(required = false) String sdt,
                                 @RequestParam(required = false) String trangThai,
                                 @RequestParam(required = false) String loai,
                                 @RequestParam(required = false) String hinhThuc,
                                 Model model){
        Pageable pageable = PageRequest.of(page, size, Sort.by("ngayTao").descending());
        Page<HoaDon> pageHoaDon = hoaDonService.findWithFilters(sdt, trangThai, loai, hinhThuc, pageable);

        model.addAttribute("dsHoaDon", pageHoaDon.getContent());
        model.addAttribute("pageHoaDon", pageHoaDon);

        model.addAttribute("sdt", sdt);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("loai", loai);
        model.addAttribute("hinhThuc", hinhThuc);

        model.addAttribute("dsTrangThai", List.of("CHO_XAC_NHAN", "DA_XAC_NHAN", "DA_GIAO_HANG","THANH_CONG","THAT_BAI"));
        model.addAttribute("dsLoai", List.of("OFFLINE", "Online"));
        model.addAttribute("dsHinhThuc", List.of("TIEN_MAT", "CHUYEN_KHOAN"));
        return "admin/LichSuHoaDon";
    }

    @GetMapping("/admin/lich-su-hoa-don/lich-su/{id}")
    public String lichsu(@PathVariable Integer id, Model model){
        List<LichSuHoaDon> lichSuHoaDons =  hoaDonService.getLichSuHoaDonByHoaDonId(id);
        model.addAttribute("lichSuList", lichSuHoaDons);
        List<NhanVien> nhanVienList = nhanVienService.getAll();

        return "admin/ChiTietLichSu";
    }

}
