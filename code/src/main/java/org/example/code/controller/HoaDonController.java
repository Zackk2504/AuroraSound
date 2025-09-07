package org.example.code.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.dom4j.DocumentException;
import org.example.code.model.HoaDon;
import org.example.code.model.HoaDonChiTiet;
import org.example.code.service.HoaDonChiTietService;
import org.example.code.service.HoaDonPdfExporter;
import org.example.code.service.HoaDonService;
import org.example.code.service.VoucherService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/ban-hang-tai-quay/hoa-don")
public class HoaDonController {
    @Autowired
    private HoaDonService hoaDonService;
    @Autowired
    private HoaDonChiTietService hoaDonChiTietService;
    @Autowired
    private VoucherService  voucherService;

    @GetMapping()
    public String xemChiTietHoaDon(  @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(required = false) String sdt,
                                     @RequestParam(required = false) String trangThai,
                                     @RequestParam(required = false) String loai,
                                     @RequestParam(required = false) String hinhThuc,
                                     Model model) {
//        List<HoaDon> list = hoaDonService.getAllHoaDon();
//        model.addAttribute("listHoaDon", list);
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
        return "admin/hoaDon";
    }

    @GetMapping("/pdf/{id}")
    public void exportPdf(@PathVariable("id") Integer id, HttpServletResponse response) throws IOException, DocumentException, com.itextpdf.text.DocumentException {
        HoaDon hoaDon = hoaDonService.getHoaDonById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại"));
        List<HoaDonChiTiet> chiTietList = hoaDonChiTietService.getListHoaDonChiTietByIdHoaDon(id);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=hoa-don-" + id + ".pdf");
        System.out.println("Exporting PDF for HoaDon ID: " + id);

        BigDecimal tienGiam = voucherService.tinhTienGiam(hoaDon.getIdVoucher(),hoaDon.getThanhTien());
        HoaDonPdfExporter exporter = new HoaDonPdfExporter(hoaDon, chiTietList,tienGiam);
        exporter.export(response);
    }
}
