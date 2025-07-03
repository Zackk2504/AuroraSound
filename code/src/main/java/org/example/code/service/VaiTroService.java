package org.example.code.service;

import jakarta.annotation.PostConstruct;
import org.example.code.model.VaiTro;
import org.example.code.repo.VaiTroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VaiTroService {
    @Autowired
    private VaiTroRepository vaiTroRepository;

    public List<VaiTro> getAllVaiTros() {
        return vaiTroRepository.findAll();
    }

    @PostConstruct
    public void initDefaultRoles() {
        createRoleIfNotExists("admin");
        createRoleIfNotExists("employee");
    }

    private void createRoleIfNotExists(String tenVaiTro) {
        Optional<VaiTro> existing = vaiTroRepository.findByTenVaiTro(tenVaiTro);
        if (existing.isEmpty()) {
            VaiTro vaiTro = new VaiTro();
            vaiTro.setTenVaiTro(tenVaiTro);
            vaiTroRepository.save(vaiTro);
            System.out.println("Đã tạo vai trò: " + tenVaiTro);
        }
    }
    public VaiTro getVaiTroByName(String tenVaiTro) {
        return vaiTroRepository.findByTenVaiTro(tenVaiTro)
                .orElseThrow(() -> new RuntimeException("Vai trò không tồn tại: " + tenVaiTro));
    }


}
