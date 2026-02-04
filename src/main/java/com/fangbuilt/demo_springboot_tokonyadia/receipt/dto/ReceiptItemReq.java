package com.fangbuilt.demo_springboot_tokonyadia.receipt.dto;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ReceiptItemReq(
    @NotNull(message = "Product ID tidak boleh kosong")
    UUID productId,

    @NotNull(message = "Quantity tidak boleh kosong")
    @Min(value = 1, message = "Quantity minimal 1")
    Integer quantity) {
}
