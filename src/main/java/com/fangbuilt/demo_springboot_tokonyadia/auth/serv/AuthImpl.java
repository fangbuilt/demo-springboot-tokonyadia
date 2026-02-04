package com.fangbuilt.demo_springboot_tokonyadia.auth.serv;

import com.fangbuilt.demo_springboot_tokonyadia.auth.dto.AuthRes;
import com.fangbuilt.demo_springboot_tokonyadia.auth.dto.LoginReq;
import com.fangbuilt.demo_springboot_tokonyadia.auth.dto.RegisterReq;
import com.fangbuilt.demo_springboot_tokonyadia.auth.JwtUtil;
import com.fangbuilt.demo_springboot_tokonyadia.member.MemberNtt;
import com.fangbuilt.demo_springboot_tokonyadia.member.MemberRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthImpl implements AuthServ {

	private final MemberRepo memberRepo;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public AuthRes register(RegisterReq request) {
		if (memberRepo.existsByUsername(request.username())) {
			throw new ResponseStatusException(
					HttpStatus.CONFLICT,
					"Username '" + request.username() + "' sudah digunakan");
		}

		String hashedPassword = passwordEncoder.encode(request.password());

		MemberNtt newMember = MemberNtt.builder()
				.username(request.username())
				.password(hashedPassword)
				.role(MemberNtt.MemberRole.USER)
				.active(true)
				.build();

		MemberNtt savedMember = memberRepo.save(newMember);

		String accessToken = jwtUtil.generateToken(
				savedMember.getUsername(),
				savedMember.getRole().name());
		String refreshToken = jwtUtil.generateRefreshToken(savedMember.getUsername());

		return AuthRes.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.tokenType("Bearer")
				.username(savedMember.getUsername())
				.role(savedMember.getRole().name())
				.build();
	}

	@Override
	public AuthRes login(LoginReq request) {
		MemberNtt member = memberRepo.findByUsername(request.username())
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.UNAUTHORIZED,
						"Username atau password salah"));

		if (member.getDeletedAt() != null) {
			throw new ResponseStatusException(
					HttpStatus.UNAUTHORIZED,
					"Akun tidak ditemukan");
		}

		if (!member.getActive()) {
			throw new ResponseStatusException(
					HttpStatus.UNAUTHORIZED,
					"Akun tidak aktif. Hubungi administrator.");
		}

		if (!passwordEncoder.matches(request.password(), member.getPassword())) {
			throw new ResponseStatusException(
					HttpStatus.UNAUTHORIZED,
					"Username atau password salah");
		}

		String accessToken = jwtUtil.generateToken(
				member.getUsername(),
				member.getRole().name());
		String refreshToken = jwtUtil.generateRefreshToken(member.getUsername());

		return AuthRes.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.tokenType("Bearer")
				.username(member.getUsername())
				.role(member.getRole().name())
				.build();
	}

	@Override
	public AuthRes refresh(String refreshToken) {
		try {
			String username = jwtUtil.extractUsername(refreshToken);

			MemberNtt member = memberRepo.findByUsername(username)
					.orElseThrow(() -> new ResponseStatusException(
							HttpStatus.UNAUTHORIZED,
							"Member tidak ditemukan"));

			if (!jwtUtil.validateToken(refreshToken, username)) {
				throw new ResponseStatusException(
						HttpStatus.UNAUTHORIZED,
						"Refresh token tidak valid");
			}

			if (member.getDeletedAt() != null || !member.getActive()) {
				throw new ResponseStatusException(
						HttpStatus.UNAUTHORIZED,
						"Akun tidak aktif");
			}

			String newAccessToken = jwtUtil.generateToken(
					member.getUsername(),
					member.getRole().name());

			return AuthRes.builder()
					.accessToken(newAccessToken)
					.refreshToken(refreshToken)
					.tokenType("Bearer")
					.username(member.getUsername())
					.role(member.getRole().name())
					.build();

		} catch (Exception e) {
			throw new ResponseStatusException(
					HttpStatus.UNAUTHORIZED,
					"Refresh token tidak valid atau sudah expired");
		}
	}

	/**
	 * Logout member
	 *
	 * Simple implementation: logout cukup di frontend (hapus token dari
	 * localStorage/cookie)
	 * Karena JWT itu stateless, server ga nyimpen token, jadi ga bisa "revoke"
	 * token
	 *
	 */
	@Override
	public void logout(String username) {
		// Simple implementation: nothing to do di server side
		// Client cukup hapus token dari storage

		// Advanced implementation (optional):
		// 1. Ambil token dari SecurityContext
		// 2. Masukin token ke blacklist table
		// 3. Set expiry blacklist sama dengan token expiry

		// For now, we keep it simple
	}
}
