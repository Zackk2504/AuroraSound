package org.example.code.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "HoaDon")
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_khachHang")
    private KhachHang idKhachhang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_nhanVien")
    private NhanVien idNhanvien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_voucher")
    private Voucher idVoucher;

    @Column(name = "giaTriThanhToan")
    private Double giaTriThanhToan;

    @Column(name = "thanhTien")
    private Double thanhTien;

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

}