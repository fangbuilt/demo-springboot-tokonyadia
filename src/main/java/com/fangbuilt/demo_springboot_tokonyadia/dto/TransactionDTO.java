package com.fangbuilt.demo_springboot_tokonyadia.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
  private UUID id;
  private LocalDateTime timestamp;
  private UUID customerId;
}
