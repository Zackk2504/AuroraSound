package org.example.code.repo;

import org.example.code.model.XuatXu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface XuatXuRepository extends JpaRepository<XuatXu, Integer> {
    List<XuatXu> findAllByTrangThai(boolean trangThai);
}