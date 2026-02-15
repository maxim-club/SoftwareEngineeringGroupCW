package com.studyspaces.spacefinder.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth") // Base URL: localhost:8080/api/auth
@CrossOrigin(origins = "*")
public class UserController {

    // TODO: Inject your UserService here once you create it!
    // private final UserService userService;

    // Endpoint: POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        // MOCK LOGIC: Replace this with real database checks later
        if ("admin".equals(username) && "password".equals(password)) {
            return ResponseEntity.ok(Map.of("message", "Login successful", "role", "admin"));
        } else {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
        }
    }

    // Endpoint: POST /api/auth/signup
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> signupData) {
        // TODO: Send this data to your database
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }
}