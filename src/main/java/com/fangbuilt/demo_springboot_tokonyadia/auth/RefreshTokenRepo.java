package com.fangbuilt.demo_springboot_tokonyadia.auth;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.fangbuilt.demo_springboot_tokonyadia.member.MemberNtt;

public interface RefreshTokenRepo extends JpaRepository<RefreshTokenNtt, UUID> {
  Optional<RefreshTokenNtt> findByToken(String token);

  @Modifying
  void deleteByMember(MemberNtt member);

}
