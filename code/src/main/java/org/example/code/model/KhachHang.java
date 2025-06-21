package org.example.code.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "KhachHang")
public class KhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "hoTen", length = 100)
    private String hoTen;

    @Column(name = "gioiTinh")
    private String gioiTinh;

    @Nationalized
    @Column(name = "soDT", length = 20)
    private String soDT;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_DiaChi")
    private DiaChi idDiachi;

    @Nationalized
    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "ngaySinh")
    private String ngaySinh;

    @Nationalized
    @Column(name = "tenDangNhap", length = 50)
    private String tenDangNhap;

    @Nationalized
    @Column(name = "matKhau", length = 100)
    private String matKhau;

    @Column(name = "trangThai")
    private Boolean trangThai;

}