package org.example.code.repo;

import org.example.code.model.MauSac;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MauSacRepository extends JpaRepository<MauSac, Integer> {
    List<MauSac> findAllByTrangThai(Boolean trangThai);
}