package com.fangbuilt.demo_springboot_tokonyadia.service;

import java.util.List;
import java.util.UUID;

import com.fangbuilt.demo_springboot_tokonyadia.dto.TransactionDTO;
import com.fangbuilt.demo_springboot_tokonyadia.entity.Transaction;

public interface TransactionService {
  Transaction create(TransactionDTO transactionDTO);

  List<Transaction> read();

  Transaction read(UUID id);

  Transaction update(UUID id, TransactionDTO transactionDTO);

  void delete(UUID id);
}
