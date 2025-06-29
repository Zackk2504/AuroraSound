package org.example.code;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "org.example.code.model")
public class CodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeApplication.class, args);
    }

}
