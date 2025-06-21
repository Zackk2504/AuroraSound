package org.example.code.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "GioHangChiTiet")
public class GioHangChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_gioHang")
    private GioHang idGiohang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_sanPhamChiTiet")
    private SanPhamChiTiet idSanphamchitiet;

    @Column(name = "soLuong")
    private Integer soLuong;

}