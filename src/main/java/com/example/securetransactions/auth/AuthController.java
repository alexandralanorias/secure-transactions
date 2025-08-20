package com.example.securetransactions.auth;

import com.example.securetransactions.user.AppUser;
import com.example.securetransactions.user.Role;
import com.example.securetransactions.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username already exists"));
        }
        String hash = passwordEncoder.encode(req.getPassword());
        AppUser user = new AppUser(req.getUsername(), hash, Set.of(Role.ROLE_USER));
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "Registered. Use HTTP Basic to authenticate."));
    }
}
