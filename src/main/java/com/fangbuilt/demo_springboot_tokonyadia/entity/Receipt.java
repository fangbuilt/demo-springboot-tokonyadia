package com.fangbuilt.demo_springboot_tokonyadia.entity;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
public class Receipt {
  @Id
  @GeneratedValue
  @UuidGenerator
  private UUID id;

  @Column(nullable = false)
  private Double cogs;

  @Column(nullable = false)
  private Integer quantity;

  @ManyToOne
  @JoinColumn(name = "product_id")
  @JsonBackReference("product-receipts")
  private Product product;

  @ManyToOne
  @JoinColumn(name = "transaction_id")
  @JsonBackReference("transaction-receipts")
  private Transaction transaction;
}
