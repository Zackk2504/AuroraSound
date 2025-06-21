package org.example.code.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "PhienBan")
public class PhienBan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "maPhienBan", length = 50)
    private String maPhienBan;

    @Nationalized
    @Column(name = "tenPhienBan", length = 100)
    private String tenPhienBan;

    @Column(name = "trangThai")
    private Boolean trangThai;

}