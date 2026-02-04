package com.fangbuilt.demo_springboot_tokonyadia.auth.config;

import com.fangbuilt.demo_springboot_tokonyadia.auth.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security Configuration - Main setup Spring Security
 *
 * Yang disetup di sini:
 * 1. Password encoder (BCrypt)
 * 2. Authentication manager (buat login)
 * 3. Security filter chain (aturan akses endpoint & JWT filter)
 *
 * Analogi: Ini kayak blueprint sistem keamanan nightclub lu
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Enable @PreAuthorize di controller
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    /**
     * Password Encoder - BCrypt buat hash password
     * JANGAN PERNAH simpan password plain text!
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Authentication Manager - Dipakai saat login buat validasi username & password
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Security Filter Chain - Aturan akses & setup JWT filter
     *
     * Yang disetup:
     * 1. CSRF disabled (karena pake JWT, ga perlu CSRF protection)
     * 2. Session management: STATELESS (ga pake session, semua info ada di JWT)
     * 3. Authorize requests: endpoint mana yang public, mana yang perlu auth
     * 4. JWT filter: dijalanin sebelum Spring Security authentication filter
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF - Ga perlu karena JWT udah cukup sebagai protection
                .csrf(csrf -> csrf.disable())

                // Atur authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Endpoint public (ga perlu login)
                        .requestMatchers(
                                "/api/auth/**" // Semua endpoint auth (login, register, refresh)
                        ).permitAll()

                        // Endpoint yang perlu role ADMIN
                        // Contoh: .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        // atau pake @PreAuthorize("hasRole('ADMIN')") di controller

                        // Semua endpoint lainnya harus authenticated
                        .anyRequest().authenticated())

                // Session management: STATELESS
                // Artinya Spring Security ga bikin session, semua auth info ada di JWT
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Add JWT filter SEBELUM UsernamePasswordAuthenticationFilter
                // Jadi setiap request lewat JWT filter dulu buat validasi token
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
