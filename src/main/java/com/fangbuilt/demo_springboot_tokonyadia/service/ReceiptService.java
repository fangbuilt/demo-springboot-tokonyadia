package com.fangbuilt.demo_springboot_tokonyadia.service;

import java.util.List;
import java.util.UUID;

import com.fangbuilt.demo_springboot_tokonyadia.dto.ReceiptDTO;
import com.fangbuilt.demo_springboot_tokonyadia.entity.Receipt;

public interface ReceiptService {
  Receipt create(ReceiptDTO receiptDTO);
  List<Receipt> read();
  Receipt read(UUID id);
  Receipt update(UUID id, ReceiptDTO receiptDTO);
  void delete(UUID id);
}
