package org.example.code.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.dom4j.DocumentException;
import org.example.code.model.HoaDon;
import org.example.code.model.HoaDonChiTiet;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class HoaDonPdfExporter {
    private final HoaDon hoaDon;
    private final List<HoaDonChiTiet> chiTietList;

    public HoaDonPdfExporter(HoaDon hoaDon, List<HoaDonChiTiet> chiTietList) {
        this.hoaDon = hoaDon;
        this.chiTietList = chiTietList;
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException, com.itextpdf.text.DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        String fontPath = "src/main/resources/static/arial.ttf"; // hoặc đường dẫn tuyệt đối trong máy
        BaseFont baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font fontVN = new Font(baseFont, 12);
        Font titleFont = new Font(baseFont, 16, Font.BOLD);

        Paragraph title = new Paragraph("HÓA ĐƠN BÁN HÀNG\n\n", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph("Hóa đơn #: " + hoaDon.getId(), fontVN));
        document.add(new Paragraph("Người mua: " + hoaDon.getTenNguoiMua(), fontVN));
        document.add(new Paragraph("SĐT: " + hoaDon.getSdtNguoiMua(), fontVN));
        document.add(new Paragraph("Địa chỉ: " + hoaDon.getDiaChiNhanHang(), fontVN));
        document.add(new Paragraph("Hình thức: " + hoaDon.getHinhThucThanhToan(), fontVN));
        document.add(new Paragraph("\n"));

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        table.addCell(new Phrase("Sản phẩm", fontVN));
        table.addCell(new Phrase("SL", fontVN));
        table.addCell(new Phrase("Đơn giá", fontVN));
        table.addCell(new Phrase("Thành tiền", fontVN));

        for (HoaDonChiTiet ct : chiTietList) {
            table.addCell(new Phrase(ct.getIdSanphamchitiet().getIdSanpham().getTenSanPham(), fontVN));
            table.addCell(new Phrase(ct.getSoLuong().toString(), fontVN));
            table.addCell(new Phrase(ct.getDonGia().toString(), fontVN));

            BigDecimal soLuong = BigDecimal.valueOf(ct.getSoLuong());
            BigDecimal donGia = (ct.getDonGia());
            BigDecimal thanhTien = soLuong.multiply(donGia);

            table.addCell(new Phrase(thanhTien.toString(), fontVN));
        }
        document.add(table);


        Paragraph total = new Paragraph("\nTổng thanh toán: " + hoaDon.getThanhTien() + " đ", fontVN);
        document.add(total);
        document.close();
    }
}
