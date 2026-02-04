package com.fangbuilt.demo_springboot_tokonyadia.transaction.serv;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.fangbuilt.demo_springboot_tokonyadia.customer.CustomerNtt;
import com.fangbuilt.demo_springboot_tokonyadia.customer.CustomerRepo;
import com.fangbuilt.demo_springboot_tokonyadia.product.ProductNtt;
import com.fangbuilt.demo_springboot_tokonyadia.product.ProductRepo;
import com.fangbuilt.demo_springboot_tokonyadia.receipt.ReceiptNtt;
import com.fangbuilt.demo_springboot_tokonyadia.receipt.dto.ReceiptItemReq;
import com.fangbuilt.demo_springboot_tokonyadia.receipt.dto.ReceiptItemRes;
import com.fangbuilt.demo_springboot_tokonyadia.transaction.TransactionNtt;
import com.fangbuilt.demo_springboot_tokonyadia.transaction.TransactionRepo;
import com.fangbuilt.demo_springboot_tokonyadia.transaction.TransactionSpec;
import com.fangbuilt.demo_springboot_tokonyadia.transaction.dto.TransactionReq;
import com.fangbuilt.demo_springboot_tokonyadia.transaction.dto.TransactionRes;

import lombok.RequiredArgsConstructor;

/**
 * TransactionService dengan business logic lengkap:
 *
 * 1. SNAPSHOT PRICE (COGS): Harga di-capture saat pembelian, gak berubah
 * selamanya
 * 2. STOCK MANAGEMENT: Stok berkurang otomatis, validasi sebelum pembelian
 * 3. TRANSACTION ROLLBACK: Kalau ada yang error, semua di-rollback
 * (all-or-nothing)
 *
 * Kenapa pakai @Transactional(rollbackFor = Exception.class)?
 * - Semua operasi dalam 1 method jadi atomic (semua sukses atau semua gagal)
 * - Kalau ada error di tengah-tengah (misal stok habis), database auto-rollback
 * - Gak ada kemungkinan "setengah jadi" (misal transaction tersimpan tapi stok
 * gak berkurang)
 */
@Service
@RequiredArgsConstructor
public class TransactionImpl implements TransactionServ {

  private final TransactionRepo transactionRepo;
  private final CustomerRepo customerRepo;
  private final ProductRepo productRepo;

  /**
   * Create transaction dengan logic:
   * 1. Validasi customer exists
   * 2. Validasi semua produk exists & stok cukup
   * 3. Snapshot harga saat ini ke COGS
   * 4. Kurangi stok produk
   * 5. Save transaction + receipts
   *
   * PENTING: @Transactional bikin semua ini atomic.
   * Kalau ada 1 step gagal, SEMUA di-rollback.
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public TransactionRes create(TransactionReq payload) {
    CustomerNtt customer = customerRepo.findById(payload.customerId())
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Customer dengan ID " + payload.customerId() + " tidak ditemukan"));

    TransactionNtt transaction = TransactionNtt.builder()
        .customer(customer)
        .receipts(new ArrayList<>())
        .build();

    List<ReceiptNtt> receipts = new ArrayList<>();

    for (ReceiptItemReq item : payload.items()) {
      ProductNtt product = productRepo.findById(item.productId())
          .orElseThrow(() -> new ResponseStatusException(
              HttpStatus.NOT_FOUND,
              "Produk dengan ID " + item.productId() + " tidak ditemukan"));

      if (product.getStock() < item.quantity()) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            String.format(
                "Stok produk '%s' tidak mencukupi. Tersedia: %d, Diminta: %d",
                product.getName(),
                product.getStock(),
                item.quantity()));
      }

      Double snapshotPrice = product.getCogm();

      product.setStock(product.getStock() - item.quantity());
      productRepo.save(product); // Update stok

      ReceiptNtt receipt = ReceiptNtt.builder()
          .cogs(snapshotPrice) // Price snapshot
          .quantity(item.quantity())
          .product(product)
          .transaction(transaction)
          .build();

      receipts.add(receipt);
    }

    transaction.setReceipts(receipts);

    // Save transaction, cascade save receipts
    TransactionNtt savedTransaction = transactionRepo.save(transaction);

    return toResponse(savedTransaction);
  }

  @Override
  @Transactional(readOnly = true)
  public TransactionRes read(UUID id) {
    TransactionNtt transaction = transactionRepo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Transaction dengan ID " + id + " tidak ditemukan"));
    return toResponse(transaction);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<TransactionRes> read(
      UUID customerId,
      String customerName,
      LocalDateTime startDate,
      LocalDateTime endDate,
      Pageable pageable) {
    Specification<TransactionNtt> spec = (root, query, cb) -> cb.conjunction();

    if (customerId != null) {
      spec = spec.and(TransactionSpec.hasCustomerId(customerId));
    }
    if (customerName != null && !customerName.isBlank()) {
      spec = spec.and(TransactionSpec.hasCustomerNameLike(customerName));
    }
    if (startDate != null || endDate != null) {
      spec = spec.and(TransactionSpec.hasDateRange(startDate, endDate));
    }

    return transactionRepo.findAll(spec, pageable).map(this::toResponse);
  }

  /**
   * Delete transaction - HARD DELETE only.
   * Kenapa gak soft delete?
   * - Transaction adalah financial record yang harus permanent buat audit
   * - Soft delete cuma buat data yang "bisa dihapus tapi mau di-keep"
   * - Untuk transaction, kalau mau "cancel", lebih baik bikin refund transaction
   * baru
   *
   * Method ini mostly buat development/testing. Di production, disable atau limit
   * access.
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(UUID id) {
    if (!transactionRepo.existsById(id)) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "Transaction dengan ID " + id + " tidak ditemukan");
    }

    transactionRepo.deleteById(id);
  }

  private TransactionRes toResponse(TransactionNtt transaction) {
    List<ReceiptItemRes> items = transaction.getReceipts().stream()
        .map(receipt -> ReceiptItemRes.builder()
            .id(receipt.getId())
            .productId(receipt.getProduct().getId())
            .productName(receipt.getProduct().getName())
            .cogs(receipt.getCogs()) // Snapshot price
            .quantity(receipt.getQuantity())
            .subtotal(receipt.getCogs() * receipt.getQuantity())
            .build())
        .toList();

    Double totalAmount = items.stream()
        .mapToDouble(ReceiptItemRes::subtotal)
        .sum();

    return TransactionRes.builder()
        .id(transaction.getId())
        .timestamp(transaction.getCreatedAt())
        .customerId(transaction.getCustomer().getId())
        .customerName(transaction.getCustomer().getFullname())
        .items(items)
        .totalAmount(totalAmount)
        .createdAt(transaction.getCreatedAt())
        .updatedAt(transaction.getUpdatedAt())
        .build();
  }
}
