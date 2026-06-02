package com.acc.ACC_AYCE.security;

import com.acc.ACC_AYCE.Entity.Student;
import com.acc.ACC_AYCE.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // First try to find as student
        try {
            Student student = studentRepository.findByEmail(email).orElse(null);
            if (student != null) {
                return User.builder()
                        .username(student.getEmail())
                        .password(student.getPassword())
                        .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + student.getRole())))
                        .build();
            }
        } catch (Exception e) {
            // Continue to fallback users for testing
        }
        
        // Fallback users for testing purposes
        if (email.equals("admin@example.com")) {
            return User.builder()
                    .username("admin@example.com")
                    .password("$2a$10$1pUfRgcvw1LG23S8UxEAFOAUNhyMhYGmvOK4zT9T.v5vhr6vsHUUy") // password: password123
                    .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")))
                    .build();
        }
        
        if (email.equals("registrar@example.com")) {
            return User.builder()
                    .username("registrar@example.com")
                    .password("$2a$10$1pUfRgcvw1LG23S8UxEAFOAUNhyMhYGmvOK4zT9T.v5vhr6vsHUUy") // password: password123
                    .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_REGISTRAR")))
                    .build();
        }
        
        if (email.equals("student@example.com")) {
            return User.builder()
                    .username("student@example.com")
                    .password("$2a$10$1pUfRgcvw1LG23S8UxEAFOAUNhyMhYGmvOK4zT9T.v5vhr6vsHUUy") // password: password123
                    .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_STUDENT")))
                    .build();
        }
        
        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}
