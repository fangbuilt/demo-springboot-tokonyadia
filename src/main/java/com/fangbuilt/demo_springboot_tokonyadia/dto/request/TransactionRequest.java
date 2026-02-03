package com.fangbuilt.demo_springboot_tokonyadia.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionRequest(
    LocalDateTime timestamp,
    UUID customerId) {

}
