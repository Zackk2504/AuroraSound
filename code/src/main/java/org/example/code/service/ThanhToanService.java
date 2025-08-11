package org.example.code.service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.code.model.GioHangChiTiet;
import org.example.code.model.HoaDon;
import org.example.code.model.HoaDonChiTiet;
import org.example.code.model.KhachHang;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;

import java.util.List;

@Service
public class ThanhToanService {
    private final PayOS payOS;
    private final HoaDonService hoaDonService;
    private final MailService mailService;
    private final GioHangChiTietService gioHangChiTietService;
    private final HoaDonChiTietService hoaDonChiTietService;

    public ThanhToanService(PayOS payOS, HoaDonService hoaDonService, MailService mailService,
                            GioHangChiTietService gioHangChiTietService, HoaDonChiTietService hoaDonChiTietService) {
        this.payOS = payOS;
        this.hoaDonService = hoaDonService;
        this.mailService = mailService;
        this.gioHangChiTietService = gioHangChiTietService;
        this.hoaDonChiTietService = hoaDonChiTietService;
    }
    public String thanhToanChuyenKhoan(HoaDon hoaDon, KhachHang khachHang,List<GioHangChiTiet> gioHangDaChon) {
//        hoaDonService.addAndEdit(hoaDon);
//        String baseUrl = this.getBaseUrl(request);
        mailService.sendThankYouEmail(khachHang.getHoTen(), khachHang.getEmail(), hoaDon.getMaHoaDon());

        try {
            String productName = "Thanh toán đơn hàng " + hoaDon.getMaHoaDon();
            String description = "Thanh toán hóa đơn #" + hoaDon.getMaHoaDon();
            String returnUrl =  "http://localhost:8080/api/success/"+hoaDon.getId();
            String cancelUrl = "http://localhost:8080/api/cancel/"+ hoaDon.getId();

            long orderCode = System.currentTimeMillis() % 1_000_000;

            ItemData item = ItemData.builder()
                    .name(productName)
                    .quantity(1)
                    .price(hoaDon.getGiaTriThanhToan().intValue())
                    .build();

            PaymentData paymentData = PaymentData.builder()
                    .orderCode(orderCode)
                    .amount(hoaDon.getGiaTriThanhToan().intValue())
                    .description(description)
                    .returnUrl(returnUrl)
                    .cancelUrl(cancelUrl)
                    .item(item)
                    .build();
            for (GioHangChiTiet gh : gioHangDaChon) {
                HoaDonChiTiet hdct = new HoaDonChiTiet();
                hdct.setIdHoadon(hoaDon);
                hdct.setIdSanphamchitiet(gh.getIdSanphamchitiet());
                hdct.setSoLuong(gh.getSoLuong());
                hdct.setDonGia(gh.getIdSanphamchitiet().getDonGia());

                hoaDonChiTietService.addAndEdit(hdct);
                gioHangChiTietService.xoaGioHangChiTiet(gh.getId()); // Xóa sản phẩm khỏi giỏ hàng
            }
            CheckoutResponseData data = payOS.createPaymentLink(paymentData);
            return "redirect:" + data.getCheckoutUrl();

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/loi-thanh-toan";
        }
    }
    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();

        String url = scheme + "://" + serverName;
        if ((scheme.equals("http") && serverPort != 80) || (scheme.equals("https") && serverPort != 443)) {
            url += ":" + serverPort;
        }
        url += contextPath;
        return url;
    }
}
