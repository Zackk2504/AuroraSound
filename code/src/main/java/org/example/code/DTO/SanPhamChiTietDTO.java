package org.example.code.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SanPhamChiTietDTO {
    private Integer id;
    private Integer idSanpham;
    private Integer idMausac;
    private Integer idPhienban;
    private String anhSP;
    private Integer soLuongTon;
    private BigDecimal donGia;
    private String moTa;
    private String trangThai;
}
