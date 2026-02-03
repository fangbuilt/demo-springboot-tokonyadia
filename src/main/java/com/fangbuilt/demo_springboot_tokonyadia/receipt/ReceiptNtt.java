package com.fangbuilt.demo_springboot_tokonyadia.receipt;

import com.fangbuilt.demo_springboot_tokonyadia.common.ntt.BaseNtt;
import com.fangbuilt.demo_springboot_tokonyadia.product.ProductNtt;
import com.fangbuilt.demo_springboot_tokonyadia.transaction.TransactionNtt;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Receipt = detail item dalam satu transaksi (shopping cart items).
 *
 * SUPER PENTING:
 * COGS = Cost of Goods Sold = SNAPSHOT harga produk SAAT PEMBELIAN.
 * Jadi kalau seller nanti ubah harga di Product.cogm, data receipt TIDAK
 * berubah.
 * Ini penting buat akurasi accounting dan history transaksi.
 *
 * Contoh real-world:
 * - Hari ini Product.cogm = 100.000
 * - Customer beli, Receipt.cogs = 100.000 (snapshot)
 * - Besok seller ubah Product.cogm jadi 150.000
 * - Receipt.cogs tetep 100.000 (gak berubah!)
 *
 * Soft delete: Tidak (receipt harus permanent buat accounting).
 */
@Entity
@Table(name = "m_receipts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReceiptNtt extends BaseNtt {

  /**
   * COGS = Harga snapshot saat pembelian (TIDAK boleh berubah selamanya!)
   */
  @Column(nullable = false)
  private Double cogs;

  @Column(nullable = false)
  private Integer quantity;

  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  @JsonBackReference("product-receipts")
  private ProductNtt product;

  @ManyToOne
  @JoinColumn(name = "transaction_id", nullable = false)
  @JsonBackReference("transaction-receipts")
  private TransactionNtt transaction;
}
