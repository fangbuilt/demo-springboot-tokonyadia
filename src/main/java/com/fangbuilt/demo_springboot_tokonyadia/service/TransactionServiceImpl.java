package com.fangbuilt.demo_springboot_tokonyadia.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fangbuilt.demo_springboot_tokonyadia.dto.TransactionDTO;
import com.fangbuilt.demo_springboot_tokonyadia.entity.Customer;
import com.fangbuilt.demo_springboot_tokonyadia.entity.Transaction;
import com.fangbuilt.demo_springboot_tokonyadia.repository.CustomerRepository;
import com.fangbuilt.demo_springboot_tokonyadia.repository.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService {
  private final TransactionRepository transactionRepository;
  private final CustomerRepository customerRepository;

  public TransactionServiceImpl(TransactionRepository transactionRepository, CustomerRepository customerRepository) {
    this.transactionRepository = transactionRepository;
    this.customerRepository = customerRepository;
  }

  @Override
  public Transaction create(TransactionDTO transactionDTO) {
    Transaction transaction = new Transaction();
    transaction.setTimestamp(transactionDTO.getTimestamp());

    if (transactionDTO.getCustomerId() != null) {
      Customer customer = customerRepository.findById(transactionDTO.getCustomerId()).orElse(null);
      transaction.setCustomer(customer);
    }

    return transactionRepository.save(transaction);
  }

  @Override
  public List<Transaction> read() {
    return transactionRepository.findAll();
  }

  @Override
  public Transaction read(UUID id) {
    return transactionRepository.findById(id).orElse(null);
  }

  @Override
  public Transaction update(UUID id, TransactionDTO transactionDTO) {
    Transaction existingTransaction = transactionRepository.findById(id).orElse(null);
    if (existingTransaction == null) {
      return null;
    }
    existingTransaction.setTimestamp(transactionDTO.getTimestamp());

    if (transactionDTO.getCustomerId() != null) {
      Customer customer = customerRepository.findById(transactionDTO.getCustomerId()).orElse(null);
      existingTransaction.setCustomer(customer);
    }

    return transactionRepository.save(existingTransaction);
  }

  @Override
  public void delete(UUID id) {
    transactionRepository.deleteById(id);
  }
}
