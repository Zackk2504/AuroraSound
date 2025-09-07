package org.example.code.controller;

import org.example.code.model.Voucher;
import org.example.code.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/vouchers")
public class VoucherController {
    @Autowired
    private VoucherService voucherService;

    @GetMapping
    public String getAllVouchers(Model model) {
        List<Voucher> list =  voucherService.getAllVouchers();
        model.addAttribute("vouchers", list);
        return "admin/voucher";
    }

    @GetMapping("/{id}")
    public String getVoucherById(Model model , @PathVariable Integer id) {
        Optional<Voucher> voucher = voucherService.getVoucherById(id);
        model.addAttribute("voucher", voucher.orElse(new Voucher()));
        return "admin/voucher";
    }

    @PostMapping("/add")
    public String addVoucher(@ModelAttribute Voucher voucher) {
         voucherService.add(voucher);
         return "redirect:/admin/vouchers";
    }

    @PostMapping("/edit/{id}")
    public String updateVoucher(@PathVariable Integer id, @ModelAttribute Voucher voucher) {
        Optional<Voucher> existing = voucherService.getVoucherById(id);
        if (existing.isPresent()) {
            voucher.setId(id);
            voucherService.updateVoucher(id, voucher);
        }
        return "redirect:/admin/vouchers";
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
    @PostMapping("/stop/{id}")
    public String stopVoucher(@PathVariable Integer id) {
        voucherService.stopvoucher(id);
        System.out.println("hello" + id);
        return "redirect:/admin/vouchers";
    }
}

