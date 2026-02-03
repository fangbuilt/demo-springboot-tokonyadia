package com.fangbuilt.demo_springboot_tokonyadia.controller;

import com.fangbuilt.demo_springboot_tokonyadia.dto.request.CustomerRequest;
import com.fangbuilt.demo_springboot_tokonyadia.dto.response.CustomerResponse;
import com.fangbuilt.demo_springboot_tokonyadia.service.CustomerService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

  private final CustomerService customerService;

  @PostMapping
  public ResponseEntity<CustomerResponse> create(@RequestBody CustomerRequest payload) {
    CustomerResponse response = customerService.create(payload);
    // Balikin 201 CREATED (lebih semantik daripada 200 OK buat create data)
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CustomerResponse> read(@PathVariable UUID id) {
    return ResponseEntity.ok(customerService.read(id));
  }

  @GetMapping
  public ResponseEntity<Page<CustomerResponse>> read(
      @RequestParam(required = false) String fullname,
      @RequestParam(required = false) String email,
      Pageable pageable // ?page=0&size=10&sort=fullname,desc
  ) {
    return ResponseEntity.ok(customerService.read(fullname, email, pageable));
  }

  @PutMapping("/{id}")
  public ResponseEntity<CustomerResponse> update(@PathVariable UUID id, @RequestBody CustomerRequest payload) {
    return ResponseEntity.ok(customerService.update(id, payload));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    customerService.delete(id);
    // Balikin 204 NO CONTENT (Standard buat delete sukses)
    return ResponseEntity.noContent().build();
  }
}