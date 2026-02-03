package com.fangbuilt.demo_springboot_tokonyadia.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fangbuilt.demo_springboot_tokonyadia.dto.TransactionDTO;
import com.fangbuilt.demo_springboot_tokonyadia.entity.Transaction;
import com.fangbuilt.demo_springboot_tokonyadia.service.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
  private final TransactionService transactionService;

  public TransactionController(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  @PostMapping
  public Transaction create(@RequestBody TransactionDTO transactionDTO) {
    return transactionService.create(transactionDTO);
  }

  @GetMapping
  public List<Transaction> read() {
    return transactionService.read();
  }

  @GetMapping("/{id}")
  public Transaction read(@PathVariable UUID id) {
    return transactionService.read(id);
  }

  @PutMapping("/{id}")
  public Transaction update(@PathVariable UUID id, @RequestBody TransactionDTO transactionDTO) {
    return transactionService.update(id, transactionDTO);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable UUID id) {
    transactionService.delete(id);
  }
}
