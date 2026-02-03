package com.fangbuilt.demo_springboot_tokonyadia.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptDTO {
  private UUID id;
  private Double cogs;
  private Integer quantity;
  private UUID productId;
  private UUID transactionId;
}
