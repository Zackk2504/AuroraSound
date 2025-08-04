package org.example.code.DTO;

public class SanPhamBienTheDTO {
    public Integer idSanPham;
    public Long soLuongBienThe;

    public SanPhamBienTheDTO(Integer idSanPham, Long soLuongBienThe) {
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
