package org.example.code.service;

import org.example.code.model.VaiTro;
import org.example.code.repo.VaiTroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VaiTroService {
    @Autowired
    private VaiTroRepository vaiTroRepository;

    public List<VaiTro> getAllVaiTros() {
        return vaiTroRepository.findAll();
    }


}
