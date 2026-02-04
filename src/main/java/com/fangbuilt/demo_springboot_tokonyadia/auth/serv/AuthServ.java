package com.fangbuilt.demo_springboot_tokonyadia.auth.serv;

import com.fangbuilt.demo_springboot_tokonyadia.auth.dto.AuthRes;
import com.fangbuilt.demo_springboot_tokonyadia.auth.dto.LoginReq;
import com.fangbuilt.demo_springboot_tokonyadia.auth.dto.RegisterReq;

/**
 * Auth Service Interface
 * Pattern yang sama kayak CustomerServ, MemberServ, dll
 */
public interface AuthServ {
    
    /**
     * Register member baru
     * @param request Data registrasi (username, password)
     * @return Response dengan tokens
     */
    AuthRes register(RegisterReq request);
    
    /**
     * Login member
     * @param request Data login (username, password)
     * @return Response dengan tokens
     */
    AuthRes login(LoginReq request);
    
    /**
     * Refresh access token pake refresh token
     * @param refreshToken Refresh token yang valid
     * @return Response dengan access token baru & refresh token yang sama
     */
    AuthRes refresh(String refreshToken);
    
    /**
     * Logout member
     * Untuk implementasi simple, logout cukup di frontend (hapus token dari storage)
     * @param username Username yang mau logout
     */
    void logout(String username);
}
