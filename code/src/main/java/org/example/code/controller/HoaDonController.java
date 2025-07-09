package org.example.code.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.dom4j.DocumentException;
import org.example.code.model.HoaDon;
import org.example.code.model.HoaDonChiTiet;
import org.example.code.service.HoaDonChiTietService;
import org.example.code.service.HoaDonPdfExporter;
import org.example.code.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/hoa-don")
public class HoaDonController {
    @Autowired
    private HoaDonService hoaDonService;
    @Autowired
    private HoaDonChiTietService hoaDonChiTietService;

    @GetMapping()
    public String xemChiTietHoaDon( Model model) {
        List<HoaDon> list = hoaDonService.getAllHoaDon();
        model.addAttribute("listHoaDon", list);
        return "admin/hoaDon";
    }

    @GetMapping("/pdf/{id}")
    public void exportPdf(@PathVariable("id") Integer id, HttpServletResponse response) throws IOException, DocumentException, com.itextpdf.text.DocumentException {
        HoaDon hoaDon = hoaDonService.getHoaDonById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại"));        List<HoaDonChiTiet> chiTietList = hoaDonChiTietService.getListHoaDonChiTietByIdHoaDon(id);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=hoa-don-" + id + ".pdf");

        HoaDonPdfExporter exporter = new HoaDonPdfExporter(hoaDon, chiTietList);
        exporter.export(response);
    }
}
