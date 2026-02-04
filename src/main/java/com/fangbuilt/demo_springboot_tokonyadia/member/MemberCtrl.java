package com.fangbuilt.demo_springboot_tokonyadia.member;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fangbuilt.demo_springboot_tokonyadia.member.dto.MemberReq;
import com.fangbuilt.demo_springboot_tokonyadia.member.dto.MemberRes;
import com.fangbuilt.demo_springboot_tokonyadia.member.serv.MemberServ;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberCtrl {

  private final MemberServ memberServ;

  @PostMapping
  public ResponseEntity<MemberRes> create(@Valid @RequestBody MemberReq payload) {
    MemberRes response = memberServ.create(payload);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<MemberRes> read(@PathVariable UUID id) {
    return ResponseEntity.ok(memberServ.read(id));
  }

  @GetMapping
  public ResponseEntity<Page<MemberRes>> read(
      @RequestParam(required = false) String username,
      @RequestParam(required = false) Boolean hasCustomerProfile,
      Pageable pageable) {
    return ResponseEntity.ok(memberServ.read(username, hasCustomerProfile, pageable));
  }

  @PutMapping("/{id}")
  public ResponseEntity<MemberRes> update(
      @PathVariable UUID id,
      @Valid @RequestBody MemberReq payload) {
    return ResponseEntity.ok(memberServ.update(id, payload));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    memberServ.delete(id);
    return ResponseEntity.noContent().build();
  }
}
