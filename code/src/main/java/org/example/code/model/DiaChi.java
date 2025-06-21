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
    @Column(name = "soNha", length = 100)
    private String soNha;

    @Nationalized
    @Column(name = "quanHuyenXaThanhPho")
    private String quanHuyenXaThanhPho;

}