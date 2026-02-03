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

import com.fangbuilt.demo_springboot_tokonyadia.dto.ReceiptDTO;
import com.fangbuilt.demo_springboot_tokonyadia.entity.Receipt;
import com.fangbuilt.demo_springboot_tokonyadia.service.ReceiptService;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {
  private final ReceiptService receiptService;

  public ReceiptController(ReceiptService receiptService) {
    this.receiptService = receiptService;
  }

  @PostMapping
  public Receipt create(@RequestBody ReceiptDTO receiptDTO) {
    return receiptService.create(receiptDTO);
  }

  @GetMapping
  public List<Receipt> read() {
    return receiptService.read();
  }

  @GetMapping("/{id}")
  public Receipt read(@PathVariable UUID id) {
    return receiptService.read(id);
  }

  @PutMapping("/{id}")
  public Receipt update(@PathVariable UUID id, @RequestBody ReceiptDTO receiptDTO) {
    return receiptService.update(id, receiptDTO);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable UUID id) {
    receiptService.delete(id);
  }
}
