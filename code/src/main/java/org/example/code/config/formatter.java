package org.example.code.config;


import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.Locale;

@Component("utils")
public class formatter {

    public String formatCurrency(Number number) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatter.format(number) + "₫";
    }
}
