package org.example.code.repo;

import org.example.code.model.PhienBan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhienBanRepository extends JpaRepository<PhienBan, Integer> {
    List<PhienBan> findAllByTrangThai(Boolean trangThai);
}