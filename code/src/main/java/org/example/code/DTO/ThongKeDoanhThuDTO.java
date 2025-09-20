package org.example.code.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ThongKeDoanhThuDTO {
    private String label;   // Ngày/Tháng/Quý...
    private BigDecimal doanhThu;
}
