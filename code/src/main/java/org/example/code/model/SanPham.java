package org.example.code.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "SanPham")
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_loaiSanPham")
    private LoaiSanPham idLoaisanpham;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_thuongHieu")
    private ThuongHieu idThuonghieu;

    @ManyToOne(fetch = FetchType.EAGER)
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
    private String trangThai;

    @OneToMany(mappedBy = "idSanpham", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<SanPhamChiTiet> sanPhamChiTietList = new ArrayList<>();


}