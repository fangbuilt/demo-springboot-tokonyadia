package com.fangbuilt.demo_springboot_tokonyadia.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class JwtConfig {
	@Value("${jwt.secret:trN023eu8sKdrTBQZIzPuLt6ng+AJ46wLb702P1xSw0=}")
	private String secretKey;

	@Value("${jwt.expiration:86400000}") // Default 24 jam (dalam milliseconds)
	private Long expiration;

	@Value("${jwt.refresh-expiration:604800000}") // Default 7 hari
	private Long refreshExpiration;
}
