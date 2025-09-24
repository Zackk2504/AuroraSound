package org.example.code.DTO;

public class SanPhamBienTheDTO {
    public Integer idSanPham;
    public Integer soLuongBienThe;

    public SanPhamBienTheDTO(Integer idSanPham, Integer soLuongBienThe) {
        this.idSanPham = idSanPham;
        this.soLuongBienThe = soLuongBienThe;
    }

    @Override
    public String toString() {
        return "SanPhamBienTheDTO{" +
                "idSanPham=" + idSanPham +
                ", soLuongBienThe=" + soLuongBienThe +
                '}';
    }
}
