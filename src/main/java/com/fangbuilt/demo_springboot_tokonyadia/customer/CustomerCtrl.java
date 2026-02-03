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

/**
 * Controller untuk Customer endpoints dengan comprehensive filtering.
 */
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerCtrl {

    private final CustomerServ customerServ;

    @PostMapping
    public ResponseEntity<CustomerRes> create(@Valid @RequestBody CustomerReq payload) {
        CustomerRes response = customerServ.create(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerRes> read(@PathVariable UUID id) {
        return ResponseEntity.ok(customerServ.read(id));
    }

    /**
     * Get customers dengan pagination dan filtering.
     *
     * Query params:
     * - fullname: Filter by name (partial match)
     * - email: Filter by email (partial match)
     * - address: Filter by address (partial match)
     * - gender: Filter by gender (MALE/FEMALE)
     * - hasMember: Filter by member account status (true/false)
     * - page, size, sort: Pagination params
     *
     * Example: GET
     * /customers?fullname=john&gender=MALE&hasMember=true&page=0&size=10&sort=fullname,asc
     */
    @GetMapping
    public ResponseEntity<Page<CustomerRes>> read(
            @RequestParam(required = false) String fullname,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) Boolean hasMember,
            Pageable pageable) {
        return ResponseEntity.ok(
                customerServ.read(fullname, email, address, gender, hasMember, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerRes> update(
            @PathVariable UUID id,
            @Valid @RequestBody CustomerReq payload) {
        return ResponseEntity.ok(customerServ.update(id, payload));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        customerServ.delete(id);
        return ResponseEntity.noContent().build();
    }
}
