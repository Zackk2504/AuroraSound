package org.example.code.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "HoaDon")
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_khachHang")
    private KhachHang idKhachhang;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_nhanVien")
    private NhanVien idNhanvien;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_voucher")
    private Voucher idVoucher;

    @Column(name = "giaTriThanhToan")
    private BigDecimal giaTriThanhToan;

    @Column(name = "thanhTien")
    private BigDecimal thanhTien;

    @Nationalized
    @Column(name = "hinhThucThanhToan", length = 100)
    private String hinhThucThanhToan;

    @Column(name = "trangThaiHoaDon")
    private String trangThaiHoaDon;

    @Nationalized
    @Column(name = "diaChiNhanHang")
    private String diaChiNhanHang;

    @Nationalized
    @Column(name = "tenNguoiNhan", length = 100)
    private String tenNguoiNhan;

    @Nationalized
    @Column(name = "sdtNguoiNhan", length = 20)
    private String sdtNguoiNhan;

    @Nationalized
    @Column(name = "loaiHoaDon", length = 50)
    private String loaiHoaDon;

    @Nationalized
    @Column(name = "ghiChu")
    private String ghiChu;

    @Nationalized
    @Column(name = "tenNguoiMua", length = 100)
    private String tenNguoiMua;

    @Nationalized
    @Column(name = "sdtNguoiMua", length = 100)
    private String sdtNguoiMua;

    @Column(name = "tienship")
    private BigDecimal tienship;

    @Column(name = "ngayTao")
    private LocalDateTime ngayTao;

    @Column(name = "maHoaDon")
    private String maHoaDon;

    @Column(name = "tienTraSau")
    private BigDecimal tienTraSau;
}