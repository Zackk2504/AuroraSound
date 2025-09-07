package org.example.code.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "LichSuHoaDon")
public class LichSuHoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "trangThaiCu")
    private String trangThaiCu;

    @Column(name = "trangThaiMoi")
    private String trangThaiMoi;

    @Column(name = "ngayCapNhat")
    private LocalDateTime ngayCapNhat;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nguoiCapNhat")
    private NhanVien nhanVien;

    @Nationalized
    @Column(name = "ghiChu")
    private String ghiChu;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDHoaDon")
    private HoaDon hoaDon;

}