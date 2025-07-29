package org.example.code.DTO;

import lombok.Data;

@Data
public class ShippingFeeRequest {
    private String province;
    private String district;
    private String ward;
    private int weight;
}