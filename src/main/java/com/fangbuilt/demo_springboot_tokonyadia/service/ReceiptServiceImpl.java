package com.fangbuilt.demo_springboot_tokonyadia.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fangbuilt.demo_springboot_tokonyadia.dto.ReceiptDTO;
import com.fangbuilt.demo_springboot_tokonyadia.entity.Product;
import com.fangbuilt.demo_springboot_tokonyadia.entity.Receipt;
import com.fangbuilt.demo_springboot_tokonyadia.entity.Transaction;
import com.fangbuilt.demo_springboot_tokonyadia.repository.ProductRepository;
import com.fangbuilt.demo_springboot_tokonyadia.repository.ReceiptRepository;
import com.fangbuilt.demo_springboot_tokonyadia.repository.TransactionRepository;

@Service
public class ReceiptServiceImpl implements ReceiptService {
  private final ReceiptRepository receiptRepository;
  private final ProductRepository productRepository;
  private final TransactionRepository transactionRepository;

  public ReceiptServiceImpl(ReceiptRepository receiptRepository, ProductRepository productRepository, TransactionRepository transactionRepository) {
    this.receiptRepository = receiptRepository;
    this.productRepository = productRepository;
    this.transactionRepository = transactionRepository;
  }

  @Override
  public Receipt create(ReceiptDTO receiptDTO) {
    Receipt receipt = new Receipt();
    receipt.setCogs(receiptDTO.getCogs());
    receipt.setQuantity(receiptDTO.getQuantity());
    
    if (receiptDTO.getProductId() != null) {
      Product product = productRepository.findById(receiptDTO.getProductId()).orElse(null);
      receipt.setProduct(product);
    }
    
    if (receiptDTO.getTransactionId() != null) {
      Transaction transaction = transactionRepository.findById(receiptDTO.getTransactionId()).orElse(null);
      receipt.setTransaction(transaction);
    }
    
    return receiptRepository.save(receipt);
  }

  @Override
  public List<Receipt> read() {
    return receiptRepository.findAll();
  }

  @Override
  public Receipt read(UUID id) {
    return receiptRepository.findById(id).orElse(null);
  }

  @Override
  public Receipt update(UUID id, ReceiptDTO receiptDTO) {
    Receipt existingReceipt = receiptRepository.findById(id).orElse(null);
    if (existingReceipt == null) {
      return null;
    }
    existingReceipt.setCogs(receiptDTO.getCogs());
    existingReceipt.setQuantity(receiptDTO.getQuantity());
    
    if (receiptDTO.getProductId() != null) {
      Product product = productRepository.findById(receiptDTO.getProductId()).orElse(null);
      existingReceipt.setProduct(product);
    }
    
    if (receiptDTO.getTransactionId() != null) {
      Transaction transaction = transactionRepository.findById(receiptDTO.getTransactionId()).orElse(null);
      existingReceipt.setTransaction(transaction);
    }
    
    return receiptRepository.save(existingReceipt);
  }

  @Override
  public void delete(UUID id) {
    receiptRepository.deleteById(id);
  }
}
