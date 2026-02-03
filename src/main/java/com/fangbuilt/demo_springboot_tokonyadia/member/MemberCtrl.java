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

/**
 * Controller untuk Member endpoints.
 *
 * Endpoints:
 * POST /members - Create member
 * GET /members/{id} - Get single member
 * GET /members - Get paginated members with filters
 * PUT /members/{id} - Update member
 * DELETE /members/{id} - Soft delete member
 */
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberCtrl {

  private final MemberServ serv;

  @PostMapping
  public ResponseEntity<MemberRes> create(@Valid @RequestBody MemberReq req) {
    MemberRes response = serv.create(req);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<MemberRes> read(@PathVariable UUID id) {
    return ResponseEntity.ok(serv.read(id));
  }

  /**
   * Get members dengan pagination dan filtering.
   *
   * Query params:
   * - username: Filter by username (partial match)
   * - hasCustomerProfile: Filter by customer profile status (true/false)
   * - page, size, sort: Pagination params (dari Pageable)
   *
   * Example: GET
   * /members?username=john&hasCustomerProfile=true&page=0&size=10&sort=username,asc
   */
  @GetMapping
  public ResponseEntity<Page<MemberRes>> read(
      @RequestParam(required = false) String username,
      @RequestParam(required = false) Boolean hasCustomerProfile,
      Pageable pageable) {
    return ResponseEntity.ok(serv.read(username, hasCustomerProfile, pageable));
  }

  @PutMapping("/{id}")
  public ResponseEntity<MemberRes> update(
      @PathVariable UUID id,
      @Valid @RequestBody MemberReq request) {
    return ResponseEntity.ok(serv.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    serv.delete(id);
    return ResponseEntity.noContent().build();
  }
}
