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

@Entity
@Table(name = "m_receipts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReceiptNtt extends BaseNtt {

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
