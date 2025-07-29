package org.example.code.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "DiaChi")
public class DiaChi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "diaChi", length = 100)
    private String diaChi;

    @Column(name = "province_id")
    private Integer provinceId;

    @Column(name = "district_id")
    private Integer districtId;

    @Column(name = "ward_code")
    private Integer wardCode;

    @Column(name = "macDinh")
    private Boolean macdinh;

    @JoinColumn(name = "ID_KhachHang", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private KhachHang khachHang;

}