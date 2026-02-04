package com.fangbuilt.demo_springboot_tokonyadia.customer.serv;

import com.fangbuilt.demo_springboot_tokonyadia.common.util.Gender;
import com.fangbuilt.demo_springboot_tokonyadia.customer.CustomerNtt;
import com.fangbuilt.demo_springboot_tokonyadia.customer.CustomerRepo;
import com.fangbuilt.demo_springboot_tokonyadia.customer.CustomerSpec;
import com.fangbuilt.demo_springboot_tokonyadia.customer.dto.CustomerReq;
import com.fangbuilt.demo_springboot_tokonyadia.customer.dto.CustomerRes;

import com.fangbuilt.demo_springboot_tokonyadia.member.MemberNtt;
import com.fangbuilt.demo_springboot_tokonyadia.member.MemberRepo;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * CustomerService dengan soft delete dan comprehensive filtering.
 * Customer bisa punya Member account (registered user) atau gak punya (guest
 * checkout).
 */
@Service
@RequiredArgsConstructor
public class CustomerImpl implements CustomerServ {

  private final CustomerRepo customerRepo;
  private final MemberRepo memberRepo;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public CustomerRes create(CustomerReq payload) {
    CustomerNtt customer = CustomerNtt.builder()
        .fullname(payload.fullname())
        .email(payload.email())
        .address(payload.address())
        .gender(payload.gender())
        .build();

    if (payload.memberId() != null) {
      MemberNtt member = memberRepo.findById(payload.memberId())
          .orElseThrow(() -> new ResponseStatusException(
              HttpStatus.NOT_FOUND,
              "Member dengan ID " + payload.memberId() + " tidak ditemukan"));
      customer.setMember(member);
    }

    return toResponse(customerRepo.save(customer));
  }

  @Override
  @Transactional(readOnly = true)
  public CustomerRes read(UUID id) {
    CustomerNtt customer = customerRepo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Customer dengan ID " + id + " tidak ditemukan"));

    if (customer.getDeletedAt() != null) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "Customer dengan ID " + id + " tidak ditemukan");
    }

    return toResponse(customer);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CustomerRes> read(
      String fullname,
      String email,
      String address,
      Gender gender,
      Boolean hasMember,
      Pageable pageable) {
    // Build dynamic specification dengan semua filter yang tersedia
    Specification<CustomerNtt> spec = CustomerSpec.excludeDeleted();

    if (fullname != null && !fullname.isBlank()) {
      spec = spec.and(CustomerSpec.hasFullnameLike(fullname));
    }
    if (email != null && !email.isBlank()) {
      spec = spec.and(CustomerSpec.hasEmailLike(email));
    }
    if (address != null && !address.isBlank()) {
      spec = spec.and(CustomerSpec.hasAddressLike(address));
    }
    if (gender != null) {
      spec = spec.and(CustomerSpec.hasGender(gender));
    }
    if (hasMember != null) {
      if (hasMember) {
        spec = spec.and(CustomerSpec.hasMemberAccount());
      } else {
        spec = spec.and(CustomerSpec.isGuest());
      }
    }

    return customerRepo.findAll(spec, pageable).map(this::toResponse);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public CustomerRes update(UUID id, CustomerReq payload) {
    CustomerNtt customer = customerRepo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Customer dengan ID " + id + " tidak ditemukan"));

    if (customer.getDeletedAt() != null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Tidak bisa update customer yang sudah dihapus");
    }

    customer.setFullname(payload.fullname());
    customer.setEmail(payload.email());
    customer.setAddress(payload.address());
    customer.setGender(payload.gender());

    if (payload.memberId() != null) {
      MemberNtt member = memberRepo.findById(payload.memberId())
          .orElseThrow(() -> new ResponseStatusException(
              HttpStatus.NOT_FOUND,
              "Member dengan ID " + payload.memberId() + " tidak ditemukan"));
      customer.setMember(member);
    } else {
      customer.setMember(null);
    }

    return toResponse(customerRepo.save(customer));
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(UUID id) {
    CustomerNtt customer = customerRepo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Customer dengan ID " + id + " tidak ditemukan"));

    if (customer.getDeletedAt() != null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Customer sudah dihapus sebelumnya");
    }

    customer.setDeletedAt(LocalDateTime.now());
    customerRepo.save(customer);
  }

  private CustomerRes toResponse(CustomerNtt customer) {
    return CustomerRes.builder()
        .id(customer.getId())
        .fullname(customer.getFullname())
        .email(customer.getEmail())
        .address(customer.getAddress())
        .gender(customer.getGender())
        .memberId(customer.getMember() != null ? customer.getMember().getId() : null)
        .createdAt(customer.getCreatedAt())
        .updatedAt(customer.getUpdatedAt())
        .build();
  }
}
