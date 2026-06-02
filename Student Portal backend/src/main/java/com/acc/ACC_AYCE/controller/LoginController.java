// Backend LoginController.java

package com.acc.ACC_AYCE.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.acc.ACC_AYCE.dto.LoginRequest;
import com.acc.ACC_AYCE.dto.LoginResponse;
import com.acc.ACC_AYCE.Entity.Enrollment;
import com.acc.ACC_AYCE.service.AuthService;
import com.acc.ACC_AYCE.util.JwtUtil;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthService authService;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            String email = request.getEmail();
            String password = request.getPassword();
            
            // Assign roles based on email for testing
            String role = "STUDENT";
            if (email.equals("admin@example.com")) {
                role = "ADMIN";
            } else if (email.equals("registrar@example.com")) {
                role = "REGISTRAR";
            } else if (email.equals("student@example.com")) {
                role = "STUDENT";
            }
            
            // Authenticate using Spring Security
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Generate JWT token after successful authentication
            String token = jwtUtil.generateToken(email, role);
            
            return ResponseEntity.ok(new LoginResponse(token, email, role));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new LoginResponse(null, null, null));
        }
    }

    @PostMapping("/register-course")
    public ResponseEntity<?> registerCourse(@RequestBody @NonNull Enrollment enrollment) {
        return ResponseEntity.ok(authService.registerCourse(enrollment));
    }

    @PostMapping("/pay-fees/{id}")
    public ResponseEntity<?> payFees(@PathVariable @NonNull Long id) {
        return ResponseEntity.ok(authService.payFees(id));
    }

    @GetMapping("/report-card/{studentId}")
    public ResponseEntity<?> getReportCard(@PathVariable Long studentId) {
        return ResponseEntity.ok(authService.viewReportCard(studentId));
    }
}