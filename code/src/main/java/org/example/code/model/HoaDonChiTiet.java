package org.example.code.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "HoaDonChiTiet")
public class HoaDonChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_hoaDon")
    private HoaDon idHoadon;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_sanPhamChiTiet")
    private SanPhamChiTiet idSanphamchitiet;

    @Column(name = "soLuong")
    private Integer soLuong;

    @Column(name = "donGia")
    private Double donGia;

}