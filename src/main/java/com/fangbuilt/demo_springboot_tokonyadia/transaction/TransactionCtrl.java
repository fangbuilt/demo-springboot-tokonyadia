package com.fangbuilt.demo_springboot_tokonyadia.transaction;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fangbuilt.demo_springboot_tokonyadia.transaction.dto.TransactionReq;
import com.fangbuilt.demo_springboot_tokonyadia.transaction.dto.TransactionRes;
import com.fangbuilt.demo_springboot_tokonyadia.transaction.serv.TransactionServ;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionCtrl {

  private final TransactionServ transactionServ;

  @PostMapping
  public ResponseEntity<TransactionRes> create(@Valid
  @RequestBody
  TransactionReq payload) {
    TransactionRes response = transactionServ.create(payload);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TransactionRes> read(@PathVariable
  UUID id) {
    return ResponseEntity.ok(transactionServ.read(id));
  }

  @GetMapping
  public ResponseEntity<Page<TransactionRes>> read(
      @RequestParam(required = false)
      UUID customerId,
      @RequestParam(required = false)
      String customerName,
      @RequestParam(required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
      LocalDateTime startDate,
      @RequestParam(required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
      LocalDateTime endDate,
      Pageable pageable) {
    return ResponseEntity.ok(
        transactionServ.read(customerId, customerName, startDate, endDate, pageable));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable
  UUID id) {
    transactionServ.delete(id);
    return ResponseEntity.noContent().build();
  }
}