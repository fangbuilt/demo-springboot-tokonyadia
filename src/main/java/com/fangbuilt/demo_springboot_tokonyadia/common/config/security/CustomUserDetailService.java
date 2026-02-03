package com.fangbuilt.demo_springboot_tokonyadia.common.config.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    if (!username.equals("admin")) {
      throw new UsernameNotFoundException("User not found");
    }
    return User.builder()
        .username("admin")
        .password("password")
        .roles("ADMIN")
        .build();
  }

}
