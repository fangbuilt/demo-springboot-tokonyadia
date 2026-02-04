package com.fangbuilt.demo_springboot_tokonyadia.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductReq(
    @NotBlank(message = "Nama produk tidak boleh kosong")
    @Size(max = 200, message = "Nama produk maksimal 200 karakter")
    String name,

    @NotNull(message = "COGM (harga pokok) tidak boleh kosong")
    @Min(value = 0, message = "COGM tidak boleh negatif")
    Double cogm,

    @NotNull(message = "Stok tidak boleh kosong")
    @Min(value = 0, message = "Stok tidak boleh negatif")
    Integer stock) {
}