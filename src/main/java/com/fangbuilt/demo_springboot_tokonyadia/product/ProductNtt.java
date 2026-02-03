package com.fangbuilt.demo_springboot_tokonyadia.product;

import java.util.List;

import com.fangbuilt.demo_springboot_tokonyadia.common.ntt.BaseNtt;
import com.fangbuilt.demo_springboot_tokonyadia.receipt.ReceiptNtt;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Product = barang yang dijual.
 * COGM = Cost of Goods Manufactured (harga pokok produk).
 *
 * PENTING: Harga ini bisa berubah sewaktu-waktu, tapi harga di Receipt
 * TIDAK boleh berubah (snapshot price saat pembelian).
 *
 * Soft delete: Iya (biar produk discontinued tetep bisa dilacak di history).
 */
@Entity
@Table(name = "m_products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductNtt extends BaseNtt {

  @Column(nullable = false, length = 200)
  private String name;

  @Column(nullable = false)
  private Double cogm; // Harga pokok produk (bisa berubah)

  @Column(nullable = false)
  private Integer stock; // Stok yang tersedia

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference("product-receipts")
  private List<ReceiptNtt> receipts;
}
