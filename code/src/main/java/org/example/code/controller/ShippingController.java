package org.example.code.controller;



import org.example.code.service.GhnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/shipping")
public class ShippingController {

    @Autowired
    private GhnService ghnService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("provinces", ghnService.getProvinces().getBody());
        return "shipping";
    }

    @ResponseBody
    @PostMapping("/districts")
    public ResponseEntity<String> getDistricts(@RequestParam int provinceId) {
        return ghnService.getDistricts(provinceId);
    }

    @ResponseBody
    @PostMapping("/wards")
    public ResponseEntity<String> getWards(@RequestParam int districtId) {
        return ghnService.getWards(districtId);
    }

    @ResponseBody
    @PostMapping("/calculate")
    public ResponseEntity<String> calculateFee(@RequestParam int districtId, @RequestParam int wardCode) {
        return ghnService.calculateShippingFee(districtId, wardCode);
    }
}
