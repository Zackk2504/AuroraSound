package org.example.code.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private BigDecimal giaTriVoucher;

    @Column(name = "giaTriToiDaApDung")
    private BigDecimal giaTriToiDaApDung;

    @Column(name = "giaTriToiThieuApDung")
    private BigDecimal giaTriToiThieuApDung;

    @Column(name = "soLuongVoucher")
    private Integer soLuongVoucher;

    @Column(name = "ngayBatDau")
    private LocalDateTime ngayBatDau;

    @Column(name = "ngayKetThuc")
    private LocalDateTime ngayKetThuc;

    @Column(name = "trangThai")
    private String trangThai;

    @Nationalized
    @Column(name = "kieuGiam", length = 50)
    private String kieuGiam;

    @Column(name = "giaTriToiDaVoucher")
    private BigDecimal giaTriToiDaVoucher;

    @Column(name = "soLuongConLai")
    private Integer soLuongConLai;

}