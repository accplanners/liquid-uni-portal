package com.acc.ACC_AYCE.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
            		.requestMatchers("/api/hello").permitAll()
            		.requestMatchers("/api/debug-auth/**").permitAll()
            		.requestMatchers("/api/debug-encode").permitAll()
            		.requestMatchers("/api/auth/login").permitAll()
            		.requestMatchers("/api/auth/register-course").hasAnyRole("STUDENT", "ADMIN", "REGISTRAR")
            		.requestMatchers("/api/auth/pay-fees/**").hasAnyRole("STUDENT", "ADMIN", "REGISTRAR")
            		.requestMatchers("/api/auth/report-card/**").hasAnyRole("STUDENT", "ADMIN", "REGISTRAR")
            		.requestMatchers("/api/course/**").permitAll()
            		.requestMatchers("/api/billing/**").permitAll()
            		.requestMatchers("/api/student/email/**", "/api/student/name/**", "/student/**").permitAll()
            		.requestMatchers("/students", "/faculty", "/courses").permitAll()
                .requestMatchers("/faculty/**").hasAnyRole("FACULTY", "ADMIN", "REGISTRAR")
                .requestMatchers("/api/attendance/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/registrar/**").hasAnyRole("REGISTRAR", "ADMIN")
                .requestMatchers("/api/student/enrollment/**").hasRole("STUDENT")
                .requestMatchers("/api/payments/**").hasAnyRole("STUDENT", "ADMIN", "REGISTRAR")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:3000", "http://localhost:3001"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
