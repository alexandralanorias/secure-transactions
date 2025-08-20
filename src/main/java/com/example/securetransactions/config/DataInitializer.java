package com.example.securetransactions.config;

import com.example.securetransactions.user.AppUser;
import com.example.securetransactions.user.Role;
import com.example.securetransactions.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(UserRepository users, PasswordEncoder encoder) {
        return args -> {
            if (!users.existsByUsername("admin")) {
                AppUser admin = new AppUser("admin", encoder.encode("AdminPass123!"), Set.of(Role.ROLE_ADMIN));
                users.save(admin);
            }
        };
    }
}
