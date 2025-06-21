package org.example.code.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "SanPhamChiTiet")
public class SanPhamChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_sanPham")
    private SanPham idSanpham;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_mauSac")
    private MauSac idMausac;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_phienBan")
    private PhienBan idPhienban;

    @Nationalized
    @Column(name = "anhSP")
    private String anhSP;

    @Column(name = "soLuongTon")
    private Integer soLuongTon;

    @Column(name = "donGia")
    private Double donGia;

    @Nationalized
    @Lob
    @Column(name = "moTa")
    private String moTa;

    @Column(name = "trangThai")
    private Boolean trangThai;

}