package org.example.code.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "XuatXu")
public class XuatXu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "maXuatXu", length = 50)
    private String maXuatXu;

    @Nationalized
    @Column(name = "noiXuatXu", length = 100)
    private String noiXuatXu;

    @Column(name = "trangThai")
    private Boolean trangThai;

}