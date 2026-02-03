package com.fangbuilt.demo_springboot_tokonyadia.member.serv;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.fangbuilt.demo_springboot_tokonyadia.member.MemberNtt;
import com.fangbuilt.demo_springboot_tokonyadia.member.MemberRepo;
import com.fangbuilt.demo_springboot_tokonyadia.member.MemberSpec;
import com.fangbuilt.demo_springboot_tokonyadia.member.dto.MemberReq;
import com.fangbuilt.demo_springboot_tokonyadia.member.dto.MemberRes;

import lombok.RequiredArgsConstructor;

/**
 * MemberService dengan username uniqueness validation dan soft delete.
 */
@Service
@RequiredArgsConstructor
public class MemberImpl implements MemberServ {

  private final MemberRepo repo;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public MemberRes create(MemberReq request) {
    // Validasi username unique
    if (repo.existsByUsername(request.username())) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Username '" + request.username() + "' sudah dipakai");
    }

    MemberNtt member = MemberNtt.builder()
        .username(request.username())
        .password(request.password()) // TODO: Hash dengan BCrypt di production
        .build();

    return toResponse(repo.save(member));
  }

  @Override
  @Transactional(readOnly = true)
  public MemberRes read(UUID id) {
    MemberNtt member = repo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Member dengan ID " + id + " tidak ditemukan"));

    if (member.getDeletedAt() != null) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "Member dengan ID " + id + " tidak ditemukan");
    }

    return toResponse(member);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<MemberRes> read(String username, Boolean hasCustomerProfile, Pageable pageable) {
    Specification<MemberNtt> spec = MemberSpec.excludeDeleted();

    if (username != null && !username.isBlank()) {
      spec = spec.and(MemberSpec.hasUsernameLike(username));
    }
    if (hasCustomerProfile != null) {
      if (hasCustomerProfile) {
        spec = spec.and(MemberSpec.hasCustomerProfile());
      } else {
        spec = spec.and(MemberSpec.hasNoCustomerProfile());
      }
    }

    return repo.findAll(spec, pageable).map(this::toResponse);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public MemberRes update(UUID id, MemberReq request) {
    MemberNtt member = repo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Member dengan ID " + id + " tidak ditemukan"));

    if (member.getDeletedAt() != null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Tidak bisa update member yang sudah dihapus");
    }

    // Validasi username unique (kalau berubah)
    if (!member.getUsername().equals(request.username())) {
      if (repo.existsByUsername(request.username())) {
        throw new ResponseStatusException(
            HttpStatus.CONFLICT,
            "Username '" + request.username() + "' sudah dipakai");
      }
    }

    member.setUsername(request.username());
    member.setPassword(request.password());

    return toResponse(repo.save(member));
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(UUID id) {
    MemberNtt member = repo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Member dengan ID " + id + " tidak ditemukan"));

    if (member.getDeletedAt() != null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Member sudah dihapus sebelumnya");
    }

    member.setDeletedAt(LocalDateTime.now());
    repo.save(member);
  }

  private MemberRes toResponse(MemberNtt member) {
    return MemberRes.builder()
        .id(member.getId())
        .username(member.getUsername())
        .createdAt(member.getCreatedAt())
        .updatedAt(member.getUpdatedAt())
        .build();
  }
}
