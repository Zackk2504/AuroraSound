package org.example.code.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dhgdzwsng"); // Thay bằng cloud name thật
        config.put("api_key", "255297584219671");      // Thay bằng API key thật
        config.put("api_secret", "BPvI5QVsqBWa7HY79WVUmcweOKk"); // Thay bằng API secret thật
        return new Cloudinary(config);
    }
}
