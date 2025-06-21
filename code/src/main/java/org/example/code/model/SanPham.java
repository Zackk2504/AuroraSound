package org.example.code.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "SanPham")
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_loaiSanPham")
    private LoaiSanPham idLoaisanpham;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_thuongHieu")
    private ThuongHieu idThuonghieu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_xuatXu")
    private XuatXu idXuatxu;

    @Nationalized
    @Column(name = "tenSanPham")
    private String tenSanPham;

    @Nationalized
    @Lob
    @Column(name = "moTa")
    private String moTa;

    @Column(name = "trangThai")
    private Boolean trangThai;

}