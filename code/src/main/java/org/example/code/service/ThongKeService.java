package org.example.code.service;

import lombok.RequiredArgsConstructor;
import org.example.code.DTO.ThongKeDoanhThuDTO;
import org.example.code.DTO.TopSanPhamDTO;
import org.example.code.model.HoaDonChiTiet;
import org.example.code.repo.HoaDonChiTietRepository;
import org.example.code.repo.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ThongKeService {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    // Doanh thu theo ngày
    public List<ThongKeDoanhThuDTO> thongKeDoanhThuTheoNgay() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return hoaDonRepository.findAll().stream()
                .filter(hd -> "THANH_CONG".equalsIgnoreCase(hd.getTrangThaiHoaDon()))
                .collect(Collectors.groupingBy(
                        hd -> hd.getNgayTao().toLocalDate().format(fmt),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                hd -> {
                                    BigDecimal giaTri = hd.getGiaTriThanhToan() != null ? hd.getGiaTriThanhToan() : BigDecimal.ZERO;
                                    BigDecimal ship = hd.getTienship() != null ? hd.getTienship() : BigDecimal.ZERO;
                                    return giaTri.subtract(ship);
                                },
                                BigDecimal::add
                        )
                ))
                .entrySet().stream()
                .map(e -> new ThongKeDoanhThuDTO(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(ThongKeDoanhThuDTO::getLabel))
                .toList();
    }


    // Top 5 sản phẩm bán chạy
    public List<TopSanPhamDTO> topSanPhamBanChay() {
        return hoaDonChiTietRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        hdct -> hdct.getIdSanphamchitiet().getIdSanpham().getTenSanPham(),
                        Collectors.summingLong(HoaDonChiTiet::getSoLuong)
                ))
                .entrySet().stream()
                .map(e -> new TopSanPhamDTO(e.getKey(), e.getValue()))
                .sorted((a, b) -> Long.compare(b.getSoLuongBan(), a.getSoLuongBan()))
                .limit(5)
                .toList();
    }
}
