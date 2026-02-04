package com.fangbuilt.demo_springboot_tokonyadia.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT Authentication Filter - Filter ini jalan di SETIAP request
 *
 * Cara kerjanya:
 * 1. Cek apakah ada "Authorization: Bearer xxx" di header
 * 2. Kalau ada, extract token nya (xxx)
 * 3. Validate token, extract username & role
 * 4. Kalau valid, set authentication di Spring Security context
 * 5. Request lanjut ke controller dengan info user yang udah authenticated
 *
 * Analogi: Kayak bouncer yang ngecek gelang VIP lu setiap mau masuk area
 * tertentu
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 1. Ambil Authorization header
        final String authHeader = request.getHeader("Authorization");

        // 2. Cek formatnya "Bearer xxx"
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Kalau ga ada atau formatnya salah, skip aja (endpoint public bakal tetep bisa
            // diakses)
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 3. Extract token (remove "Bearer " prefix)
            final String jwt = authHeader.substring(7);
            final String username = jwtUtil.extractUsername(jwt);

            // 4. Kalau username berhasil di-extract & user belum authenticated
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // 5. Validate token
                if (jwtUtil.validateToken(jwt, username)) {

                    // 6. Extract role & set authority (ROLE_ADMIN atau ROLE_USER)
                    String role = jwtUtil.extractRole(jwt);
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

                    // 7. Bikin authentication object
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            Collections.singletonList(authority));

                    // 8. Set detail dari request
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 9. Set authentication di Spring Security context
                    // Dari sini controller bisa akses info user pake SecurityContextHolder
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Kalau ada error (token invalid, expired, dll), log aja dan lanjut
            // Spring Security bakal handle nya sebagai unauthenticated request
            logger.error("JWT validation error: " + e.getMessage());
        }

        // 10. Lanjut ke filter berikutnya atau ke controller
        filterChain.doFilter(request, response);
    }
}
