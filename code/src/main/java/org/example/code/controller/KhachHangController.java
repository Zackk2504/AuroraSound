package org.example.code.controller;

import org.example.code.model.KhachHang;
import org.example.code.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@ResponseBody
public class KhachHangController {
    @Autowired
    private KhachHangService khachHangService;

    @GetMapping("/khach-hang/tim-theo-sdt")
    public ResponseEntity<?> getKhachHangBySoDT(@RequestParam("sdt") String sdt) {
        Optional<KhachHang> khach = khachHangService.getKhachHangBySDT(sdt);
        if (khach.isPresent()) {
            KhachHang kh = khach.get();
            return ResponseEntity.ok(kh);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy khách hàng với số điện thoại: " + sdt);
        }

    }
}
