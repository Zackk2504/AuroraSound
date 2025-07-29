package org.example.code.DTO;

import lombok.Data;

@Data
public class DiaChiResponse {
    Long id;
    String diaChi;
    String districtId;
    String wardCode;
    boolean macDinh;

    public DiaChiResponse(Integer id, String diaChi, Integer districtId, Integer wardCode, Boolean macdinh) {
    }
}
