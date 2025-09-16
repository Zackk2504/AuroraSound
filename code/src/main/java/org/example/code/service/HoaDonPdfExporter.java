package org.example.code.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.dom4j.DocumentException;
import org.example.code.model.HoaDon;
import org.example.code.model.HoaDonChiTiet;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class HoaDonPdfExporter {

    private final HoaDon hoaDon;
    private final List<HoaDonChiTiet> chiTietList;
    private final BigDecimal tienGiam;

    public HoaDonPdfExporter(HoaDon hoaDon, List<HoaDonChiTiet> chiTietList,BigDecimal tienGiam) {
        this.hoaDon = hoaDon;
        this.chiTietList = chiTietList;
        this.tienGiam = tienGiam;
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException, com.itextpdf.text.DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        document.open();

        // Font tiếng Việt
        String fontPath = "src/main/resources/static/arial.ttf";
        BaseFont baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font fontVN = new Font(baseFont, 12);
        Font titleFont = new Font(baseFont, 20, Font.BOLD);
        Font subTitleFont = new Font(baseFont, 16, Font.BOLD);
        Font infoFont = new Font(baseFont, 12, Font.ITALIC);

        // ===== HEADER =====
        Paragraph shopName = new Paragraph("AURORA SOUND", titleFont);
        shopName.setAlignment(Element.ALIGN_CENTER);
        document.add(shopName);

        Paragraph billTitle = new Paragraph("HÓA ĐƠN MUA HÀNG", subTitleFont);
        billTitle.setAlignment(Element.ALIGN_CENTER);
        document.add(billTitle);

        Paragraph address = new Paragraph("Địa chỉ: Trịnh Văn Bô - Nam Từ Liêm", infoFont);
        address.setAlignment(Element.ALIGN_CENTER);
        document.add(address);

        Paragraph website = new Paragraph("Website: localhost:8080/mua-hàng", infoFont);
        website.setAlignment(Element.ALIGN_CENTER);
        document.add(website);

        document.add(new Paragraph("\n"));

        // ===== THÔNG TIN HÓA ĐƠN =====
        document.add(new Paragraph("Hóa đơn #: " + hoaDon.getMaHoaDon(), fontVN));
        String tenNguoiMua = hoaDon.getTenNguoiMua();
        if (tenNguoiMua == null || tenNguoiMua.isEmpty() || tenNguoiMua.trim().equals("")) {
            tenNguoiMua = "Khách lẻ";
        }
        document.add(new Paragraph("Người mua: " + tenNguoiMua, fontVN));
        String SDTNguoiMua =  hoaDon.getSdtNguoiMua();
        if (SDTNguoiMua == null || SDTNguoiMua.trim().equals("")) {
            SDTNguoiMua = "Khách lẻ";
        }
        document.add(new Paragraph("SĐT: " +SDTNguoiMua, fontVN));
        document.add(new Paragraph("Địa chỉ: " + hoaDon.getDiaChiNhanHang(), fontVN));
        document.add(new Paragraph("Nhân viên xác nhận: " + hoaDon.getIdNhanvien().getHoTen(), fontVN));
        String hinhThucText;
        if ("TIEN_MAT".equalsIgnoreCase(hoaDon.getHinhThucThanhToan())) {
            hinhThucText = "Tiền mặt";
        } else if ("CHUYEN_KHOAN".equalsIgnoreCase(hoaDon.getHinhThucThanhToan())) {
            hinhThucText = "Chuyển khoản";
        } else {
            hinhThucText = hoaDon.getHinhThucThanhToan();
        }

        document.add(new Paragraph("Hình thức: " + hinhThucText, fontVN));
        document.add(new Paragraph("\n"));

        // ===== BẢNG SẢN PHẨM =====
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setWidths(new float[]{4f, 1.5f, 2f, 2f}); // Tỉ lệ cột

        table.addCell(new Phrase("Sản phẩm", fontVN));
        table.addCell(new Phrase("SL", fontVN));
        table.addCell(new Phrase("Đơn giá", fontVN));
        table.addCell(new Phrase("Thành tiền", fontVN));

        for (HoaDonChiTiet ct : chiTietList) {
            table.addCell(new Phrase(ct.getIdSanphamchitiet().getIdSanpham().getTenSanPham(), fontVN));
            table.addCell(new Phrase(ct.getSoLuong().toString(), fontVN));

            BigDecimal soLuong = BigDecimal.valueOf(ct.getSoLuong());
            BigDecimal donGia = ct.getDonGia();
            BigDecimal thanhTien = soLuong.multiply(donGia);

            // Format giá
            String donGiaFormatted = currencyVN.format(donGia);
            String thanhTienFormatted = currencyVN.format(thanhTien);

            table.addCell(new Phrase(donGiaFormatted, fontVN));
            table.addCell(new Phrase(thanhTienFormatted, fontVN));
        }
        document.add(table);

// ===== TỔNG TIỀN =====
        String tienGiamFormatted = currencyVN.format(tienGiam);
        String tongFormatted = currencyVN.format(hoaDon.getGiaTriThanhToan());
        String traSau = currencyVN.format(
                hoaDon.getTienTraSau() != null ? hoaDon.getTienTraSau() : BigDecimal.ZERO
        );


        Paragraph total2 = new Paragraph("\nGiảm giá: " + tienGiamFormatted, subTitleFont);
        Paragraph total = new Paragraph("\nTổng thanh toán: " + tongFormatted, subTitleFont);
        Paragraph total3 = new Paragraph("\nTiền trả sau: " + traSau, subTitleFont);

        total.setAlignment(Element.ALIGN_RIGHT);
        total2.setAlignment(Element.ALIGN_RIGHT);
        total3.setAlignment(Element.ALIGN_RIGHT);
        document.add(total2);
        document.add(total);
        document.add(total3);

        document.close();
    }

}
