package com.fangbuilt.demo_springboot_tokonyadia.transaction.dto;

import java.util.List;
import java.util.UUID;

import com.fangbuilt.demo_springboot_tokonyadia.receipt.dto.ReceiptItemReq;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO untuk create Transaction.
 *
 * Best practice: Transaction dan receipts dibuat sekaligus dalam satu request.
 * Ini karena:
 * 1. Transaksi tanpa items gak make sense
 * 2. Atomicity - semua berhasil atau semua gagal (rollback)
 * 3. Simpler untuk frontend (1 API call, bukan 2)
 */
public record TransactionReq(
    @NotNull(message = "Customer ID tidak boleh kosong")
    UUID customerId,

    @NotEmpty(message = "Transaction harus punya minimal 1 item")
    @Valid
    List<ReceiptItemReq> items // Detail barang yang dibeli
) {
}
