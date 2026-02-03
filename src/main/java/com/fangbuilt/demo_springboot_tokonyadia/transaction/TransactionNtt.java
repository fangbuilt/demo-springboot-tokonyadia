package com.fangbuilt.demo_springboot_tokonyadia.transaction;

import java.util.List;

import com.fangbuilt.demo_springboot_tokonyadia.common.ntt.BaseNtt;
import com.fangbuilt.demo_springboot_tokonyadia.customer.CustomerNtt;
import com.fangbuilt.demo_springboot_tokonyadia.receipt.ReceiptNtt;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Transaction = transaksi pembelian.
 * Timestamp pakai createdAt dari BaseEntity (otomatis ter-set).
 *
 * One Transaction has many Receipts (detail barang yang dibeli).
 * Soft delete: Tidak (transaksi harus permanent buat accounting).
 */
@Entity
@Table(name = "t_transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionNtt extends BaseNtt {

  @ManyToOne
  @JoinColumn(name = "customer_id", nullable = false)
  @JsonBackReference("customer-transactions")
  private CustomerNtt customer;

  @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference("transaction-receipts")
  private List<ReceiptNtt> receipts;

  // Timestamp udah ada dari BaseEntity.createdAt - gak perlu field lagi!
}
