package org.example.code.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;

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
    private Boolean trangThaiCu;

    @Column(name = "trangThaiMoi")
    private Boolean trangThaiMoi;

    @Column(name = "ngayCapNhat")
    private Instant ngayCapNhat;

    @Nationalized
    @Column(name = "nguoiCapNhat", length = 100)
    private String nguoiCapNhat;

    @Nationalized
    @Column(name = "ghiChu")
    private String ghiChu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDHoaDon")
    private HoaDon iDHoaDon;

}