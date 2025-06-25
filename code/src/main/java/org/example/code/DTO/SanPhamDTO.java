package org.example.code.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamDTO {
    private Integer id;
    private String tenSanPham;
    private String moTa;
    private Boolean trangThai;
    private Integer idLoaiSanPham;
    private Integer idThuongHieu;
    private Integer idXuatXu;

    private List<SanPhamChiTietDTO> chiTietList;
}
