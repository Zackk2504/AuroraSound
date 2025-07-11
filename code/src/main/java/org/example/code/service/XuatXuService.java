package org.example.code.service;

import org.example.code.model.ThuongHieu;
import org.example.code.model.XuatXu;
import org.example.code.repo.XuatXuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Service
public class XuatXuService {
    @Autowired
    private XuatXuRepository xuatXuRepository;

    public List<XuatXu> getAll(){
        return xuatXuRepository.findAll();
    }
    public Optional<XuatXu> findByID(Integer id){
        return xuatXuRepository.findById(id);
    }
    public XuatXu save(XuatXu xuatXu){
        return xuatXuRepository.save(xuatXu);
    }
}
