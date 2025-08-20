package com.example.securetransactions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class SecureTransactionsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecureTransactionsApplication.class, args);
    }
}
