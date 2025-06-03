package org.example.code.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "taikhoan")
public class Taikhoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "pass", nullable = false, length = 100)
    private String pass;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Nationalized
    @Column(name = "hoTen", length = 100)
    private String hoTen;

    @Column(name = "sdt", length = 15)
    private String sdt;

    @Column(name = "role", length = 50)
    private String role;

    @Column(name = "TrangThai", length = 10)
    private String trangThai;

    @ColumnDefault("getdate()")
    @Column(name = "created_at")
    private Instant createdAt;

}