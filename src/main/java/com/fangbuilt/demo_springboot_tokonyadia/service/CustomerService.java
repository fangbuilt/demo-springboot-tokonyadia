package com.fangbuilt.demo_springboot_tokonyadia.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fangbuilt.demo_springboot_tokonyadia.dto.request.CustomerRequest;
import com.fangbuilt.demo_springboot_tokonyadia.dto.response.CustomerResponse;

public interface CustomerService {
  CustomerResponse create(CustomerRequest payload);

  CustomerResponse read(UUID id);

  Page<CustomerResponse> read(String fullname, String email, Pageable pageable);

  CustomerResponse update(UUID id, CustomerRequest payload);

  void delete(UUID id);
}
