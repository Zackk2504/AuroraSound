package org.example.code.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "MauSac")
public class MauSac {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "maMauSac", length = 50)
    private String maMauSac;

    @Nationalized
    @Column(name = "tenMauSac", length = 100)
    private String tenMauSac;

    @Column(name = "trangThai")
    private Boolean trangThai;

}