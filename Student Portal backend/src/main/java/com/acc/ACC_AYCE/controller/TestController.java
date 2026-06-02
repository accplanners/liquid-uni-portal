package com.acc.ACC_AYCE.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class TestController {

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/hello")
    public String hello() {
        return "Backend Connected!";
    }
    
    @GetMapping("/debug-auth/{email}")
    public String debugAuth(@PathVariable String email) {
        try {
            UserDetails user = userDetailsService.loadUserByUsername(email);
            boolean passwordMatch = passwordEncoder.matches("password123", user.getPassword());
            return "User: " + user.getUsername() + ", Authorities: " + user.getAuthorities() + ", Password matches: " + passwordMatch;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    
    @PostMapping("/debug-encode")
    public String encodePassword(@RequestBody String password) {
        return passwordEncoder.encode("password123");
    }
}