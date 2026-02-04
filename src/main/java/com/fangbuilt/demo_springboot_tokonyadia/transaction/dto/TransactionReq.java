package com.fangbuilt.demo_springboot_tokonyadia.transaction.dto;

import java.util.List;
import java.util.UUID;

import com.fangbuilt.demo_springboot_tokonyadia.receipt.dto.ReceiptItemReq;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record TransactionReq(
    @NotNull(message = "Customer ID tidak boleh kosong")
    UUID customerId,

    @NotEmpty(message = "Transaction harus punya minimal 1 item")
    @Valid
    List<ReceiptItemReq> items
) {
}
