package com.fangbuilt.demo_springboot_tokonyadia.auth.serv;

import com.fangbuilt.demo_springboot_tokonyadia.auth.dto.AuthRes;
import com.fangbuilt.demo_springboot_tokonyadia.auth.dto.LoginReq;
import com.fangbuilt.demo_springboot_tokonyadia.auth.dto.RegisterReq;
import com.fangbuilt.demo_springboot_tokonyadia.auth.JwtUtil;
import com.fangbuilt.demo_springboot_tokonyadia.auth.RefreshTokenNtt;
import com.fangbuilt.demo_springboot_tokonyadia.auth.RefreshTokenRepo;
import com.fangbuilt.demo_springboot_tokonyadia.auth.config.JwtConfig;
import com.fangbuilt.demo_springboot_tokonyadia.member.MemberNtt;
import com.fangbuilt.demo_springboot_tokonyadia.member.MemberRepo;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthImpl implements AuthServ {

	private final MemberRepo memberRepo;
	private final RefreshTokenRepo refreshTokenRepo;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	private final JwtConfig jwtConfig;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public AuthRes register(RegisterReq request) {
		if (memberRepo.existsByUsername(request.username())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Username sudah digunakan");
		}

		MemberNtt newMember = MemberNtt.builder()
				.username(request.username())
				.password(passwordEncoder.encode(request.password()))
				.role(MemberNtt.MemberRole.USER)
				.active(true)
				.build();

		MemberNtt savedMember = memberRepo.save(newMember);

		String accessToken = jwtUtil.generateToken(savedMember.getUsername(), savedMember.getRole().name());

		String refreshToken = createAndSaveRefreshToken(savedMember);

		return buildResponse(accessToken, refreshToken, savedMember);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public AuthRes login(LoginReq request) {
		MemberNtt member = memberRepo.findByUsername(request.username())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username atau password salah"));

		if (member.getDeletedAt() != null || !member.getActive()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Akun tidak aktif");
		}

		if (!passwordEncoder.matches(request.password(), member.getPassword())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username atau password salah");
		}

		String accessToken = jwtUtil.generateToken(member.getUsername(), member.getRole().name());

		String refreshToken = createAndSaveRefreshToken(member);

		return buildResponse(accessToken, refreshToken, member);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public AuthRes refresh(String refreshToken) {
		RefreshTokenNtt tokenInDb = refreshTokenRepo.findByToken(refreshToken)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token tidak valid"));

		if (tokenInDb.getExpiryDate().isBefore(Instant.now())) {
			refreshTokenRepo.delete(tokenInDb);
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token expired. Login ulang.");
		}

		MemberNtt member = tokenInDb.getMember();

		if (member.getDeletedAt() != null || !member.getActive()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Akun bermasalah");
		}

		// 4. Token Rotation: Hapus token lama, buat baru (Biar aman dari maling token)
		refreshTokenRepo.delete(tokenInDb);

		String newRefreshToken = createAndSaveRefreshToken(member);
		String newAccessToken = jwtUtil.generateToken(member.getUsername(), member.getRole().name());

		return buildResponse(newAccessToken, newRefreshToken, member);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void logout(String username) {
		MemberNtt member = memberRepo.findByUsername(username)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

		refreshTokenRepo.deleteByMember(member);
	}

	private String createAndSaveRefreshToken(MemberNtt member) {
		String token = UUID.randomUUID().toString();

		RefreshTokenNtt refreshToken = RefreshTokenNtt.builder()
				.token(token)
				.member(member)
				.expiryDate(Instant.now().plusMillis(jwtConfig.getRefreshExpiration())) // Pastikan Entity pake Instant
				.build();

		refreshTokenRepo.save(refreshToken);
		return token;
	}

	private AuthRes buildResponse(String accessToken, String refreshToken, MemberNtt member) {
		return AuthRes.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.tokenType("Bearer")
				.username(member.getUsername())
				.role(member.getRole().name())
				.build();
	}
}