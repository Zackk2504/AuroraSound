package org.example.code.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.code.DTO.KhachHangDTO;
import org.example.code.model.*;
import org.example.code.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller

public class KhachHangController {
    @Autowired
    private KhachHangService khachHangService;
    @Autowired
    private GioHangChiTietService gioHangChiTietService;
    @Autowired
    private HoaDonService hoaDonService;
    @Autowired
    private DiaChiService diaChiService;
    @Autowired
    private SanPhamService sanPhamService;
    @Autowired
    private LoaiSanPhanService loaiSanPhamService;
    @Autowired
    private ThuongHieuService thuongHieuService;
    @Autowired
    private XuatXuService xuatXuService;
    @Autowired
    private AnhSanPhamService anhSanPhamService;
    @Autowired
    private GhnService shippingService;
    @Autowired
    private HoaDonChiTietService hoaDonChiTietService;
    @Autowired
    private VoucherService voucherService;


    @GetMapping("/nhan-vien/khach-hang/tim-theo-sdt")
    public ResponseEntity<?> getKhachHangBySoDT(@RequestParam("sdt") String sdt) {
        Optional<KhachHang> khach = khachHangService.getKhachHangBySDT(sdt);
        if (khach.isPresent()) {
            KhachHang kh = khach.get();
            return ResponseEntity.ok(kh);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy khách hàng với số điện thoại: " + sdt);
        }

    }

    @PostMapping("/khach-hang/thanh-toan")
    public String hienThiTrangThanhToan(@RequestParam("selectedIds") List<Integer> selectedIds,
                                        Model model) {
        List<GioHangChiTiet> gioHangDaChon = gioHangChiTietService.findByIds(selectedIds);
        model.addAttribute("gioHangDaChon", gioHangDaChon);
        BigDecimal tongTien = gioHangDaChon.stream()
                .map(item -> BigDecimal.valueOf(item.getSoLuong())
                        .multiply(item.getIdSanphamchitiet().getDonGia()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Integer khachHangId;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        KhachHang khachHang;
        if (auth.getPrincipal() instanceof DefaultOidcUser user) {
            String email = user.getAttribute("email");
            khachHang = khachHangService.getKhachHangByEmail(email).get();
        }else {
            String tenDangNhap = auth.getName();
            khachHang = khachHangService.getKhachHangByUsername(tenDangNhap).orElse(new KhachHang());
        }
        Optional <DiaChi> diaChiMacDinh = diaChiService.findByMacdinhTrue();
        if (diaChiMacDinh.isPresent()) {
            model.addAttribute("diaChiMacDinh", diaChiMacDinh.get());
        } else {
            model.addAttribute("diaChiMacDinh", new DiaChi());
        }
        model.addAttribute("khachHang", khachHang);
        model.addAttribute("tongTien", tongTien);
        model.addAttribute("listDiaChi", diaChiService.layDanhSachDiaChiTheoKhachHang(khachHang.getId()));
        model.addAttribute("VoucherList",voucherService.getvoucherBytrangThaiHoatDong());
        return "User/ThanhToan";
    }

    @PostMapping("/khach-hang/xac-nhan-thanh-toan")
    public String thanhToan(
            @RequestParam("payment") String hinhThucThanhToan,
            @RequestParam("districtId") Integer districtId,
            @RequestParam("wardCode") Integer wardCode,
            @RequestParam("diaChiNhanHang") String diaChiNhanHang,
            @RequestParam("tenNguoiNhan") String tenNguoiNhan,
            @RequestParam("sdtNguoiNhan") String sdtNguoiNhan,
            @RequestParam("selectedIds") List<Integer> selectedIds,
            @RequestParam(value = "maVoucherDaChon", required = false) Integer idvoucher
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        KhachHang khachHang;
        if (auth.getPrincipal() instanceof DefaultOidcUser user) {
            String email = user.getAttribute("email");
            khachHang = khachHangService.getKhachHangByEmail(email).get();
            System.out.println("Email: " + email);
        }else {
            String tenDangNhap = auth.getName();
            System.out.println("Tên đăng nhập: " + tenDangNhap);
            khachHang = khachHangService.getKhachHangByUsername(tenDangNhap).orElse(new KhachHang());
        }

        List<GioHangChiTiet> gioHangDaChon = gioHangChiTietService.findByIds(selectedIds);

        // Tính tổng tiền và phí ship
        ObjectMapper mapper = new ObjectMapper();

        ResponseEntity<String> tienShipResponse = shippingService.calculateShippingFee(districtId, wardCode);
        String jsonBody = tienShipResponse.getBody();  // đây là chuỗi JSON

        BigDecimal phiShip = BigDecimal.ZERO;
        BigDecimal tienVoucher = BigDecimal.ZERO;
        Voucher voucher = null;

        try {
            JsonNode root = mapper.readTree(jsonBody);
            String phiShipStr = root.path("data").path("total").asText(); // hoặc "service_fee" nếu API bạn trả vậy
            phiShip = new BigDecimal(phiShipStr);
        } catch (Exception e) {
            phiShip = BigDecimal.ZERO;
        }

        BigDecimal tongTien = gioHangDaChon.stream()
                .map(sp -> sp.getIdSanphamchitiet().getDonGia().multiply(BigDecimal.valueOf(sp.getSoLuong())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println(tongTien + " tong tien");
        if (idvoucher != null && idvoucher > 0) {
            voucher = voucherService.getVoucherById((idvoucher)).orElseThrow();
            tienVoucher = voucherService.tinhTienGiam(voucher, tongTien);
            voucherService.apdungvoucher(idvoucher);
        }
        BigDecimal tongThanhToan = tongTien.add(phiShip).subtract(tienVoucher);

        // Tạo hóa đơn
        HoaDon hoaDon = new HoaDon();
        hoaDon.setIdKhachhang(khachHang);
        hoaDon.setGiaTriThanhToan(tongTien);
        hoaDon.setThanhTien(tongThanhToan);
        hoaDon.setHinhThucThanhToan(hinhThucThanhToan);
        hoaDon.setTrangThaiHoaDon("CHO_XAC_NHAN");
        hoaDon.setDiaChiNhanHang(diaChiNhanHang);
        hoaDon.setTenNguoiNhan(tenNguoiNhan);
        hoaDon.setSdtNguoiNhan(sdtNguoiNhan);
        hoaDon.setLoaiHoaDon("Online");
        hoaDon.setIdVoucher(voucher);
        hoaDon.setTienship(phiShip);
        hoaDon.setNgayTao(java.time.LocalDateTime.now());
        hoaDon.setTenNguoiMua(khachHang.getHoTen());
        hoaDon.setSdtNguoiMua(khachHang.getSoDT());

        hoaDonService.addAndEdit(hoaDon);

        // Tạo hóa đơn chi tiết
        for (GioHangChiTiet gh : gioHangDaChon) {
            HoaDonChiTiet hdct = new HoaDonChiTiet();
            hdct.setIdHoadon(hoaDon);
            hdct.setIdSanphamchitiet(gh.getIdSanphamchitiet());
            hdct.setSoLuong(gh.getSoLuong());
            hdct.setDonGia(gh.getIdSanphamchitiet().getDonGia());

            hoaDonChiTietService.addAndEdit(hdct);
            gioHangChiTietService.xoaGioHangChiTiet(gh.getId()); // Xóa sản phẩm khỏi giỏ hàng
        }

        // Xóa sản phẩm khỏi giỏ hàng sau khi đặt hàng


        return "redirect:/khach-hang/hoa-don-thanh-cong";
    }


    @GetMapping("/khach-hang/thong-tin")
    public String hienThiThongTinCaNhan(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        KhachHang khachHang;
        if (auth.getPrincipal() instanceof DefaultOidcUser user) {
            String email = user.getAttribute("email");
             khachHang = khachHangService.getKhachHangByEmail(email).get();
            System.out.println("Email: " + email);
        }else {
            String tenDangNhap = auth.getName();
            System.out.println("Tên đăng nhập: " + tenDangNhap);
            khachHang = khachHangService.getKhachHangByUsername(tenDangNhap).orElse(new KhachHang());
        }
        System.out.println(auth.getPrincipal().getClass() + "hello");
        System.out.println(auth.getPrincipal() + "hello");


        if (khachHang != null) {
            KhachHangDTO dto = new KhachHangDTO();
            dto.setId(khachHang.getId());
            dto.setHoTen(khachHang.getHoTen());
            dto.setSoDT(khachHang.getSoDT());
            dto.setNgaySinh(khachHang.getNgaySinh());

            model.addAttribute("khachHangDTO", dto);
            model.addAttribute("danhSachDiaChi", diaChiService.layDanhSachDiaChiTheoKhachHang(khachHang.getId()));
            model.addAttribute("diaChiMoi", new DiaChi());
            model.addAttribute("listDiaChi", diaChiService.getAll());
        }
        return "User/ThongTinCaNhan";
    }

    @PostMapping("/khach-hang/cap-nhat")
    public String capNhatThongTinCaNhan(@ModelAttribute KhachHangDTO dto,
                                        RedirectAttributes redirectAttributes) {
        boolean ok = khachHangService.capNhatThongTinCaNhan(dto);
        if (ok) {
            redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin thành công!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy khách hàng.");
        }
        return "redirect:/khach-hang/thong-tin";
    }

    @GetMapping("/khach-hang/san-pham")
    public String danhSachSanPham(Model model) {
        List<SanPham> danhSach = sanPhamService.getAllSanPhams();
        List<AnhSanPham> danhSachAnh = anhSanPhamService.getAllList();
        Map<Integer, AnhSanPham> mapAnhDau = new HashMap<>();
        for (AnhSanPham anh : danhSachAnh) {
            Integer idSanPham = anh.getIdSanpham().getId();
            if (!mapAnhDau.containsKey(idSanPham)) {
                mapAnhDau.put(idSanPham, anh); // chỉ lưu ảnh đầu tiên
            }
        }
        model.addAttribute("danhSachSanPham", danhSach);
        model.addAttribute("mapAnhDau", mapAnhDau);
        model.addAttribute("dsLoai", loaiSanPhamService.getAllLoaiSanPhams());
        model.addAttribute("dsThuongHieu", thuongHieuService.getAllThuongHieus());
        model.addAttribute("dsXuatXu", xuatXuService.getAll());
        return "User/DanhSachSanPham";
    }

    @GetMapping("/khach-hang/san-pham/loc")
    public String locSanPham(
            @RequestParam(required = false) Integer loai,
            @RequestParam(required = false) Integer thuonghieu,
            @RequestParam(required = false) Integer xuatxu,
            Model model
    ) {
        List<SanPham> danhSachSanPham = sanPhamService.locSanPham(loai, thuonghieu, xuatxu);

        model.addAttribute("danhSachSanPham", danhSachSanPham);
        model.addAttribute("dsLoai", loaiSanPhamService.getAllLoaiSanPhams());
        model.addAttribute("dsThuongHieu", thuongHieuService.getAllThuongHieus());
        model.addAttribute("dsXuatXu", xuatXuService.getAll());
        model.addAttribute("loaiId", loai);
        model.addAttribute("thuongHieuId", thuonghieu);
        model.addAttribute("xuatXuId", xuatxu);

        return "User/DanhSachSanPham"; // đường dẫn đến file HTML bạn đã viết
    }

}
