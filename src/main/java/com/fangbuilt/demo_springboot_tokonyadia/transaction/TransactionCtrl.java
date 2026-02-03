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

/**
 * Controller untuk Transaction endpoints.
 *
 * PENTING: Transaction creation adalah operasi kompleks yang:
 * 1. Validasi stok produk
 * 2. Snapshot harga produk ke COGS
 * 3. Kurangi stok produk
 * 4. Save transaction + receipts
 *
 * Semuanya atomic (all-or-nothing) thanks to @Transactional.
 */
@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionCtrl {

  private final TransactionServ transactionServ;

  /**
   * Create transaction dengan receipts sekaligus.
   *
   * Request body example:
   * {
   * "customerId": "uuid-here",
   * "items": [
   * {"productId": "uuid1", "quantity": 2},
   * {"productId": "uuid2", "quantity": 1}
   * ]
   * }
   *
   * Akan otomatis:
   * - Set COGS dari Product.cogm saat ini
   * - Kurangi stok produk
   * - Rollback kalau stok tidak cukup
   */
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

  /**
   * Get transactions dengan pagination dan filtering.
   *
   * Query params:
   * - customerId: Filter by customer (exact match)
   * - customerName: Filter by customer name (partial match)
   * - startDate, endDate: Filter by date range (ISO 8601 format)
   * - page, size, sort: Pagination params
   *
   * Examples:
   * 1. Customer history: GET /transactions?customerId=uuid-here&page=0&size=10
   * 2. Search by name: GET /transactions?customerName=john&sort=createdAt,desc
   * 3. Date range: GET
   * /transactions?startDate=2026-01-01T00:00:00&endDate=2026-01-31T23:59:59
   * 4. Combine: GET /transactions?customerName=john&startDate=2026-01-01T00:00:00
   *
   * Date format: yyyy-MM-dd'T'HH:mm:ss (ISO 8601)
   * Example: 2026-01-15T14:30:00
   */
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

  /**
   * Delete transaction - HARD DELETE.
   *
   * PERHATIAN: Di production, endpoint ini sebaiknya di-disable atau
   * di-restrict ke admin only. Financial records seharusnya permanent.
   *
   * Kalau butuh "cancel transaction", lebih baik buat refund transaction baru
   * daripada delete yang existing.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable
  UUID id) {
    transactionServ.delete(id);
    return ResponseEntity.noContent().build();
  }
}