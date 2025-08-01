package org.example.code.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "SanPhamChiTiet")
public class SanPhamChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_sanPham")
    @JsonBackReference
    private SanPham idSanpham;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_mauSac")
    private MauSac idMausac;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_phienBan")
    private PhienBan idPhienban;

    @Nationalized
    @Column(name = "anhSP")
    private String anhSP;

    @Column(name = "soLuongTon")
    private Integer soLuongTon;

    @Column(name = "donGia")
    private BigDecimal donGia;

    @Nationalized
    @Lob
    @Column(name = "moTa")
    private String moTa;

    @Column(name = "trangThai")
    private String trangThai;

}