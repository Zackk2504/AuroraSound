package org.example.code.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.payos.PayOS;

@Configuration
public class PayOSConfig {

    @Bean
    public PayOS payOS() {
        // Thay các giá trị này bằng thông tin thật của anh
        String clientId = "a9e67bdb-ae7c-4173-a64a-591c7239732b";
        String apiKey = "aa336ac7-4459-4944-993e-31d370290551";
        String checksumKey = "897450d04d2343016cc4c070ab61a4bb5bf6a17d18915866d01ebca8a164f707";

        return new PayOS(clientId, apiKey, checksumKey);
    }
}

