package com.fangbuilt.demo_springboot_tokonyadia.dto.request;

public record ProductRequest(
    String name,
    Double cogm,
    Integer stock) {
}
