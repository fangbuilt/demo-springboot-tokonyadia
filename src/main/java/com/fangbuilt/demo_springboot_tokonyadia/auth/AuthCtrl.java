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

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthCtrl {

  private final AuthServ authServ;

  @PostMapping("/register")
  public ResponseEntity<AuthRes> register(@Valid
  @RequestBody
  RegisterReq request) {
    AuthRes response = authServ.register(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthRes> login(@Valid
  @RequestBody
  LoginReq request) {
    AuthRes response = authServ.login(request);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/refresh")
  public ResponseEntity<AuthRes> refresh(@Valid
  @RequestBody
  RefreshReq request) {
    AuthRes response = authServ.refresh(request.refreshToken());
    return ResponseEntity.ok(response);
  }

  @PostMapping("/logout")
  public ResponseEntity<Map<String, String>> logout() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();

    authServ.logout(username);

    Map<String, String> response = new HashMap<>();
    response.put("message", "Logout berhasil");
    return ResponseEntity.ok(response);
  }

  /**
   * Endpoint ini buat frontend bisa tau siapa yang lagi login
   * Useful buat display username, role, dll di UI
   */
  @GetMapping("/me")
  public ResponseEntity<Map<String, String>> getCurrentUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    String role = auth.getAuthorities().iterator().next().getAuthority();

    role = role.replace("ROLE_", "");

    Map<String, String> response = new HashMap<>();
    response.put("username", username);
    response.put("role", role);

    return ResponseEntity.ok(response);
  }
}
