package com.studyspaces.spacefinder.controller;

import com.studyspaces.spacefinder.model.UserRecord;
import com.studyspaces.spacefinder.service.UserManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth") // Base URL: localhost:8080/api/auth
@CrossOrigin(origins = "*")
public class UserController {

    UserManager userManager;

    public UserController(UserManager userManager) {
        this.userManager = userManager;
    }


    // Endpoint: POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        try {
            boolean valid = userManager.checkLogin(username, password);
            if (valid) {
                return ResponseEntity.ok(Map.of("message", "Login successful"));
            } else {
                return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
            }
        } catch (Exception e) {
            // User not found, DB error, etc.
            return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
        }
    }

    // Endpoint: POST /api/auth/signup
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> signupData) {

        try {
            if (userManager.signUp(signupData)) {
                return ResponseEntity.ok(Map.of("message", "User registered successfully"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("message", "Username already taken"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Unexpected Error"));
        }



    }
}