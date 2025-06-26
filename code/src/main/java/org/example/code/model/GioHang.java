package org.example.code.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "GioHang")
public class GioHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_khachHang")
    private KhachHang idKhachhang;

    @Column(name = "ngayTao")
    private LocalDate ngayTao;

    @Column(name = "ngayCapNhat")
    private LocalDate ngayCapNhat;

}