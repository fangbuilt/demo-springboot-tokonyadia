package com.fangbuilt.demo_springboot_tokonyadia.service;

import com.fangbuilt.demo_springboot_tokonyadia.dto.request.CustomerRequest;
import com.fangbuilt.demo_springboot_tokonyadia.dto.response.CustomerResponse;
import com.fangbuilt.demo_springboot_tokonyadia.entity.Customer;
import com.fangbuilt.demo_springboot_tokonyadia.entity.Member;
import com.fangbuilt.demo_springboot_tokonyadia.repository.CustomerRepository;
import com.fangbuilt.demo_springboot_tokonyadia.repository.MemberRepository;
import com.fangbuilt.demo_springboot_tokonyadia.specification.CustomerSpecification;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor // Gak perlu nulis constructor injection manual lagi
public class CustomerServiceImpl implements CustomerService {

  private final CustomerRepository customerRepository;
  private final MemberRepository memberRepository;

  @Override
  @Transactional(rollbackFor = Exception.class) // Biar kalau error, database gak corrupt
  public CustomerResponse create(CustomerRequest payload) {
    Customer customer = Customer.builder()
        .fullname(payload.fullname())
        .email(payload.email())
        .address(payload.address())
        .gender(payload.gender())
        .build();

    if (payload.memberId() != null) {
      Member member = memberRepository.findById(payload.memberId())
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));
      customer.setMember(member);
    }

    return toResponse(customerRepository.save(customer));
  }

  @Override
  @Transactional(readOnly = true) // Optimasi performa buat baca data
  public CustomerResponse read(UUID id) {
    Customer customer = customerRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
    return toResponse(customer);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CustomerResponse> read(String fullname, String email, Pageable pageable) {
    Specification<Customer> spec = (root, query, cb) -> cb.conjunction();
    if (fullname != null && !fullname.isBlank()) {
      spec = spec.and(CustomerSpecification.hasFullnameLike(fullname));
    }
    if (email != null && !email.isBlank()) {
      spec = spec.and(CustomerSpecification.hasEmailLike(email));
    }
    return customerRepository.findAll(spec, pageable).map(this::toResponse);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public CustomerResponse update(UUID id, CustomerRequest payload) {
    Customer customer = customerRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

    customer.setFullname(payload.fullname());
    customer.setEmail(payload.email());
    customer.setAddress(payload.address());
    customer.setGender(payload.gender());

    if (payload.memberId() != null) {
      Member member = memberRepository.findById(payload.memberId())
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member ID not found"));
      customer.setMember(member);
    } else {
      customer.setMember(null); // Unlink member kalau payload null
    }

    return toResponse(customerRepository.save(customer));
  }

  @Override
  public void delete(UUID id) {
    // Cek dulu ada gak, biar gak silent fail
    if (!customerRepository.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
    }
    customerRepository.deleteById(id);
  }

  // Helper method biar gak duplicate code (Mapping Entity -> Response)
  private CustomerResponse toResponse(Customer customer) {
    return CustomerResponse.builder()
        .id(customer.getId())
        .fullname(customer.getFullname())
        .email(customer.getEmail())
        .address(customer.getAddress())
        .gender(customer.getGender())
        .memberId(customer.getMember() != null ? customer.getMember().getId() : null)
        .build();
  }
}