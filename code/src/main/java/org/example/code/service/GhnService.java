package org.example.code.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
@Service
public class GhnService {

    private final String token = "dfc26f23-6b12-11f0-9b81-222185cb68c8"; // Token thật từ staging
    private final String shopId = "197187"; // ShopId từ giao diện 5sao.ghn.dev
    private final String baseUrl = "https://dev-online-gateway.ghn.vn/shiip/public-api";

    public ResponseEntity<String> getProvinces() {
        return callApi(baseUrl + "/master-data/province", HttpMethod.GET, null);
    }

    public ResponseEntity<String> getDistricts(int provinceId) {
        return callApi(baseUrl + "/master-data/district", HttpMethod.POST, Map.of("province_id", provinceId));
    }

    public ResponseEntity<String> getWards(int districtId) {
        return callApi(baseUrl + "/master-data/ward", HttpMethod.POST, Map.of("district_id", districtId));
    }

    public ResponseEntity<String> calculateShippingFee(int toDistrictId, int toWardCode) {
        Map<String, Object> body = Map.of(
                "service_type_id", 2,
                "to_district_id", toDistrictId,
                "to_ward_code", String.valueOf(toWardCode),
                "height", 15,
                "length", 15,
                "weight", 500,
                "width", 15,
                "from_district_id", 197187 // từ district id của shop
        );
        return callApi(baseUrl + "/v2/shipping-order/fee", HttpMethod.POST, body);
    }


    private ResponseEntity<String> callApi(String url, HttpMethod method, Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", token);
        headers.set("ShopId", shopId);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(body, headers);
        return new RestTemplate().exchange(url, method, entity, String.class);
    }


}


