package org.example.code.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "ThuongHieu")
public class ThuongHieu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "maThuongHieu", length = 50)
    private String maThuongHieu;

    @Nationalized
    @Column(name = "tenThuongHieu", length = 100)
    private String tenThuongHieu;

    @Column(name = "trangThai")
    private Boolean trangThai;

}