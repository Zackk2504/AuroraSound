package org.example.code.controller;

import org.example.code.model.Voucher;
import org.example.code.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vouchers")
public class VoucherController {
    @Autowired
    private VoucherService voucherService;

    @GetMapping
    public List<Voucher> getAllVouchers() {
        return voucherService.getAllVouchers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Voucher> getVoucherById(@PathVariable Integer id) {
        Optional<Voucher> voucher = voucherService.getVoucherById(id);
        return voucher.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Voucher addVoucher(@RequestBody Voucher voucher) {
        return voucherService.addVoucher(voucher);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Voucher> updateVoucher(@PathVariable Integer id, @RequestBody Voucher voucher) {
        Optional<Voucher> existing = voucherService.getVoucherById(id);
        if (existing.isPresent()) {
            return ResponseEntity.ok(voucherService.updateVoucher(id, voucher));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVoucher(@PathVariable Integer id) {
        voucherService.deleteVoucher(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/find/{maVoucher}")
    public ResponseEntity<Voucher> findByMaVoucher(@PathVariable String maVoucher) {
        Voucher voucher = voucherService.findByMaVoucher(maVoucher);
        if (voucher != null) {
            return ResponseEntity.ok(voucher);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/apply/{maVoucher}")
    public ResponseEntity<String> applyVoucher(@PathVariable String maVoucher, @RequestParam double tongTien) {
        Voucher voucher = voucherService.findByMaVoucher(maVoucher);
        if (voucherService.isVoucherApplicable(voucher, tongTien)) {
            return ResponseEntity.ok("Áp dụng thành công");
        } else {
            return ResponseEntity.badRequest().body("Voucher không hợp lệ hoặc không áp dụng được");
        }
    }
}

@Controller
class AdminVoucherViewController {
    @GetMapping("/admin/voucher")
    public String adminVoucherPage() {
        return "UI/admin/admin_voucher";
    }
}
