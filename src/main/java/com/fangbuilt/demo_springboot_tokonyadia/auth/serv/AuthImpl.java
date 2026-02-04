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

/**
 * Auth Service Implementation
 * Semua business logic authentication ada di sini
 *
 * Bekerja dengan Member entity (bukan separate User entity)
 * karena Member = authentication entity dalam system ini
 */
@Service
@RequiredArgsConstructor
public class AuthImpl implements AuthServ {

	private final MemberRepo memberRepo;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	/**
	 * Register member baru
	 * Steps:
	 * 1. Validasi username belum dipakai
	 * 2. Hash password pake BCrypt
	 * 3. Save member baru ke database dengan role USER (default)
	 * 4. Generate tokens
	 * 5. Return response dengan tokens
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public AuthRes register(RegisterReq request) {
		// 1. Cek username sudah ada atau belum
		if (memberRepo.existsByUsername(request.username())) {
			throw new ResponseStatusException(
					HttpStatus.CONFLICT,
					"Username '" + request.username() + "' sudah digunakan");
		}

		// 2. Hash password - JANGAN PERNAH simpan password plain text!
		String hashedPassword = passwordEncoder.encode(request.password());

		// 3. Bikin member baru (default role: USER, active: true)
		MemberNtt newMember = MemberNtt.builder()
				.username(request.username())
				.password(hashedPassword)
				.role(MemberNtt.MemberRole.USER)
				.active(true)
				.build();

		// 4. Save ke database
		MemberNtt savedMember = memberRepo.save(newMember);

		// 5. Generate tokens
		String accessToken = jwtUtil.generateToken(
				savedMember.getUsername(),
				savedMember.getRole().name());
		String refreshToken = jwtUtil.generateRefreshToken(savedMember.getUsername());

		// 6. Return response
		return AuthRes.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.tokenType("Bearer")
				.username(savedMember.getUsername())
				.role(savedMember.getRole().name())
				.build();
	}

	/**
	 * Login member
	 * Steps:
	 * 1. Cari member berdasarkan username
	 * 2. Validasi member active & not soft deleted
	 * 3. Validasi password
	 * 4. Generate tokens
	 * 5. Return response dengan tokens
	 */
	@Override
	public AuthRes login(LoginReq request) {
		// 1. Cari member berdasarkan username
		MemberNtt member = memberRepo.findByUsername(request.username())
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.UNAUTHORIZED,
						"Username atau password salah"));

		// 2. Cek apakah member udah di-soft delete
		if (member.getDeletedAt() != null) {
			throw new ResponseStatusException(
					HttpStatus.UNAUTHORIZED,
					"Akun tidak ditemukan");
		}

		// 3. Cek apakah member masih active
		if (!member.getActive()) {
			throw new ResponseStatusException(
					HttpStatus.UNAUTHORIZED,
					"Akun tidak aktif. Hubungi administrator.");
		}

		// 4. Validasi password
		// passwordEncoder.matches() akan hash input password terus compare dengan yang
		// di database
		if (!passwordEncoder.matches(request.password(), member.getPassword())) {
			throw new ResponseStatusException(
					HttpStatus.UNAUTHORIZED,
					"Username atau password salah");
		}

		// 5. Generate tokens
		String accessToken = jwtUtil.generateToken(
				member.getUsername(),
				member.getRole().name());
		String refreshToken = jwtUtil.generateRefreshToken(member.getUsername());

		// 6. Return response
		return AuthRes.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.tokenType("Bearer")
				.username(member.getUsername())
				.role(member.getRole().name())
				.build();
	}

	/**
	 * Refresh access token
	 * Steps:
	 * 1. Validate refresh token
	 * 2. Extract username dari refresh token
	 * 3. Cari member di database
	 * 4. Generate access token baru
	 * 5. Return response dengan access token baru (refresh token tetap sama)
	 */
	@Override
	public AuthRes refresh(String refreshToken) {
		try {
			// 1. Extract username dari refresh token
			String username = jwtUtil.extractUsername(refreshToken);

			// 2. Cari member di database
			MemberNtt member = memberRepo.findByUsername(username)
					.orElseThrow(() -> new ResponseStatusException(
							HttpStatus.UNAUTHORIZED,
							"Member tidak ditemukan"));

			// 3. Validate refresh token
			if (!jwtUtil.validateToken(refreshToken, username)) {
				throw new ResponseStatusException(
						HttpStatus.UNAUTHORIZED,
						"Refresh token tidak valid");
			}

			// 4. Cek member masih active & not deleted
			if (member.getDeletedAt() != null || !member.getActive()) {
				throw new ResponseStatusException(
						HttpStatus.UNAUTHORIZED,
						"Akun tidak aktif");
			}

			// 5. Generate access token baru
			String newAccessToken = jwtUtil.generateToken(
					member.getUsername(),
					member.getRole().name());

			// 6. Return response dengan access token baru
			// Refresh token tetep yang lama (ga perlu generate baru)
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
	 * Advanced implementation bisa pake blacklist di database/Redis
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
