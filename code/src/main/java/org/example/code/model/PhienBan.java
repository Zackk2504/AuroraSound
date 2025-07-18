package org.example.code.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhienBan that = (PhienBan) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}