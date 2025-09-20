package org.example.code.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopSanPhamDTO {
    private String tenSanPham;
    private Long soLuongBan;
}
