package org.example.code.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "NhanVien")
public class NhanVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaiTro")
    private VaiTro vaiTro;

    @Nationalized
    @Column(name = "hoTen", length = 100)
    private String hoTen;

    @Column(name = "gioiTinh")
    private Boolean gioiTinh;

    @Column(name = "ngaySinh")
    private LocalDate ngaySinh;

    @Nationalized
    @Column(name = "soDT", length = 20)
    private String soDT;

    @Nationalized
    @Column(name = "email", length = 100)
    private String email;

    @Nationalized
    @Column(name = "diaChi")
    private String diaChi;

    @Nationalized
    @Column(name = "tenDangNhap", length = 50)
    private String tenDangNhap;

    @Nationalized
    @Column(name = "matKhau", length = 100)
    private String matKhau;

    @Column(name = "trangThai")
    private Boolean trangThai;

}