package org.example.code.controller;

import org.example.code.service.GioHangChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GioHangChiTietController {
    @Autowired
    GioHangChiTietService gioHangChiTietService;

    @GetMapping("/gio-hang-chi-tiet")
    public ResponseEntity<?> getGioHangChiTiet() {
        gioHangChiTietService.addGioHangChiTiet(1, 1, 2);
        return ResponseEntity.ok("Gio hang chi tiet added successfully");
    }
}
