package com.fangbuilt.demo_springboot_tokonyadia.customer;

import com.fangbuilt.demo_springboot_tokonyadia.common.util.Gender;
import com.fangbuilt.demo_springboot_tokonyadia.customer.dto.CustomerReq;
import com.fangbuilt.demo_springboot_tokonyadia.customer.dto.CustomerRes;
import com.fangbuilt.demo_springboot_tokonyadia.customer.serv.CustomerServ;

import jakarta.validation.Valid;
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
public class CustomerCtrl {

  private final CustomerServ customerServ;

  @PostMapping
  public ResponseEntity<CustomerRes> create(@Valid
  @RequestBody
  CustomerReq payload) {
    CustomerRes response = customerServ.create(payload);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CustomerRes> read(@PathVariable
  UUID id) {
    return ResponseEntity.ok(customerServ.read(id));
  }

  @GetMapping
  public ResponseEntity<Page<CustomerRes>> read(
      @RequestParam(required = false)
      String fullname,
      @RequestParam(required = false)
      String email,
      @RequestParam(required = false)
      String address,
      @RequestParam(required = false)
      Gender gender,
      Pageable pageable) {
    return ResponseEntity.ok(
        customerServ.read(fullname, email, address, gender, pageable));
  }

  @PutMapping("/{id}")
  public ResponseEntity<CustomerRes> update(
      @PathVariable
      UUID id,
      @Valid
      @RequestBody
      CustomerReq payload) {
    return ResponseEntity.ok(customerServ.update(id, payload));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable
  UUID id) {
    customerServ.delete(id);
    return ResponseEntity.noContent().build();
  }
}
