package com.fangbuilt.demo_springboot_tokonyadia.auth;

import com.fangbuilt.demo_springboot_tokonyadia.auth.dto.AuthRes;
import com.fangbuilt.demo_springboot_tokonyadia.auth.dto.LoginReq;
import com.fangbuilt.demo_springboot_tokonyadia.auth.dto.RefreshReq;
import com.fangbuilt.demo_springboot_tokonyadia.auth.dto.RegisterReq;
import com.fangbuilt.demo_springboot_tokonyadia.auth.serv.AuthServ;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Auth Controller - Handle semua endpoint authentication
 * 
 * Endpoints:
 * - POST /api/auth/register - Register member baru
 * - POST /api/auth/login - Login
 * - POST /api/auth/refresh - Refresh access token
 * - POST /api/auth/logout - Logout (simple implementation)
 * - GET /api/auth/me - Get current user info (bonus endpoint)
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthCtrl {
    
    private final AuthServ authServ;
    
    /**
     * Register endpoint
     * 
     * Request body:
     * {
     *   "username": "john",
     *   "password": "password123"
     * }
     * 
     * Response:
     * {
     *   "accessToken": "eyJhbGc...",
     *   "refreshToken": "eyJhbGc...",
     *   "tokenType": "Bearer",
     *   "username": "john",
     *   "role": "USER"
     * }
     * 
     * Note: Setelah register, user bisa isi profile (fullname, email, address) dengan create Customer
     */
    @PostMapping("/register")
    public ResponseEntity<AuthRes> register(@Valid @RequestBody RegisterReq request) {
        AuthRes response = authServ.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Login endpoint
     * 
     * Request body:
     * {
     *   "username": "john",
     *   "password": "password123"
     * }
     * 
     * Response: sama kayak register
     */
    @PostMapping("/login")
    public ResponseEntity<AuthRes> login(@Valid @RequestBody LoginReq request) {
        AuthRes response = authServ.login(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Refresh token endpoint
     * 
     * Request body:
     * {
     *   "refreshToken": "eyJhbGc..."
     * }
     * 
     * Response:
     * {
     *   "accessToken": "eyJhbGc...", // NEW access token
     *   "refreshToken": "eyJhbGc...", // SAME refresh token
     *   "tokenType": "Bearer",
     *   "username": "john",
     *   "role": "USER"
     * }
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthRes> refresh(@Valid @RequestBody RefreshReq request) {
        AuthRes response = authServ.refresh(request.refreshToken());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Logout endpoint
     * 
     * Simple implementation: Client-side logout (hapus token dari storage)
     * Server ga perlu lakuin apa-apa karena JWT stateless
     * 
     * Headers: Authorization: Bearer <access-token>
     * 
     * Response:
     * {
     *   "message": "Logout berhasil"
     * }
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        // Ambil username dari SecurityContext (user yang sedang login)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        // Call service logout (simple implementation = nothing to do)
        authServ.logout(username);
        
        // Return success message
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logout berhasil");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get current user info endpoint (BONUS!)
     * 
     * Endpoint ini buat frontend bisa tau siapa yang lagi login
     * Useful buat display username, role, dll di UI
     * 
     * Headers: Authorization: Bearer <access-token>
     * 
     * Response:
     * {
     *   "username": "john",
     *   "role": "USER"
     * }
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> getCurrentUser() {
        // Ambil info user dari SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        String role = auth.getAuthorities().iterator().next().getAuthority();
        
        // Remove "ROLE_" prefix dari role
        role = role.replace("ROLE_", "");
        
        Map<String, String> response = new HashMap<>();
        response.put("username", username);
        response.put("role", role);
        
        return ResponseEntity.ok(response);
    }
}
