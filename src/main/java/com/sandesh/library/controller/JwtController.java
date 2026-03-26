package com.sandesh.library.controller;

import com.sandesh.library.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class JwtController {

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/get_jwt")
    public Object getToken(@RequestBody Map<String, String> request) {

        String username = request.get("username");
        String password = request.get("password");

        // 🔹 Static credentials
        if ("admin".equals(username) && "123".equals(password)) {
            String token = jwtUtil.generateToken(username);
            return Map.of("token", token);
        }

        return "Invalid credentials";
    }
}