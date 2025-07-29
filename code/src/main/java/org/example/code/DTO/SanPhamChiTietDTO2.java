package org.example.code.DTO;

import jakarta.persistence.Entity;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SanPhamChiTietDTO2 {
        private String tensanPham;
        private BigDecimal gia;
        private String tenMau;
        private String tenPhienBan;
        private String thuongHieu;
        private String xuatXu;
        private Integer soLuongTon;
        private String tenLoaiSanPham;

}
