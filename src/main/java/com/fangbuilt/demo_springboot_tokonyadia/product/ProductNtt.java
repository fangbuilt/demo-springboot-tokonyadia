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

// TODO: COGM = Harga pokok produksi sebelum ambil untung, COGS = Sesudahnya
// Selling price yg dikalikan quantity dan di sum misalnya ada diskon
// Ketiganya wajib jadi snapshot di receipt untuk audit trail
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
  private Double cogm;

  @Column(nullable = false)
  private Integer stock;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference("product-receipts")
  private List<ReceiptNtt> receipts;
}
