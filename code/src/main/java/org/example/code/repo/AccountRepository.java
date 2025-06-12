package org.example.code.repo;

import org.example.code.model.Taikhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Taikhoan,Integer> {
    Taikhoan findByUsername(String username);
    Taikhoan findByEmail(String email);
}
