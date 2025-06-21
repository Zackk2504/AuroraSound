package org.example.code.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "LoaiSanPham")
public class LoaiSanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "maLoaiSanPham", length = 50)
    private String maLoaiSanPham;

    @Nationalized
    @Column(name = "tenLoaiSanPham", length = 100)
    private String tenLoaiSanPham;

    @Column(name = "trangThai")
    private Boolean trangThai;

}