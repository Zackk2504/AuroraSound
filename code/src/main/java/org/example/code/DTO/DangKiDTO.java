package org.example.code.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DangKiDTO {
    private String UserName;
    private String email;
    private String pass;
    private String maXacNhan;
    private String hoTen;
    private String gioiTinh;
    private String soDT;
    private String ngaySinh;
}
