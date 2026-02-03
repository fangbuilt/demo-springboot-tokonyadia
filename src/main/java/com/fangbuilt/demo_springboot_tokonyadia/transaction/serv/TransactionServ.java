package com.fangbuilt.demo_springboot_tokonyadia.transaction.serv;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fangbuilt.demo_springboot_tokonyadia.transaction.dto.TransactionReq;
import com.fangbuilt.demo_springboot_tokonyadia.transaction.dto.TransactionRes;

public interface TransactionServ {
  TransactionRes create(TransactionReq payload);

  TransactionRes read(UUID id);

  Page<TransactionRes> read(UUID customerId, String customerName, LocalDateTime startDate, LocalDateTime endDate,
      Pageable pageable);

  void delete(UUID id); // Hard delete only (no soft delete for financial records)
}
