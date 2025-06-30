package org.example.code.DTO;

import lombok.Data;

@Data
public class SanPhamChiTietDTO {
    private Integer id;
    private Integer idSanpham;
    private Integer idMausac;
    private Integer idPhienban;
    private String anhSP;
    private Integer soLuongTon;
    private Double donGia;
    private String moTa;
    private String trangThai;
}
