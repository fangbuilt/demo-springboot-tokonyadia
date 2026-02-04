package com.fangbuilt.demo_springboot_tokonyadia.auth.serv;

import com.fangbuilt.demo_springboot_tokonyadia.auth.dto.AuthRes;
import com.fangbuilt.demo_springboot_tokonyadia.auth.dto.LoginReq;
import com.fangbuilt.demo_springboot_tokonyadia.auth.dto.RegisterReq;

public interface AuthServ {
  AuthRes register(RegisterReq request);

  AuthRes login(LoginReq request);

  AuthRes refresh(String refreshToken);

  void logout(String username);
}
