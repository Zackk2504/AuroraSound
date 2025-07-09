package org.example.code.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "Voucher")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "maVoucher", length = 50)
    private String maVoucher;

    @Nationalized
    @Column(name = "tenVoucher", length = 100)
    private String tenVoucher;

    @Column(name = "giaTriVoucher")
    private Double giaTriVoucher;

    @Column(name = "giaTriToiDaApDung")
    private Double giaTriToiDaApDung;

    @Column(name = "giaTriToiThieuApDung")
    private Double giaTriToiThieuApDung;

    @Column(name = "soLuongVoucher")
    private Integer soLuongVoucher;

    @Column(name = "ngayBatDau")
    private LocalDate ngayBatDau;

    @Column(name = "ngayKetThuc")
    private LocalDate ngayKetThuc;

    @Column(name = "trangThai")
    private String trangThai;

    @Nationalized
    @Column(name = "kieuGiam", length = 50)
    private String kieuGiam;

}