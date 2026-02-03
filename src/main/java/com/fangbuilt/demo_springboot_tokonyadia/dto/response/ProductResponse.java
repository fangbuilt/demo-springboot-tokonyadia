package com.fangbuilt.demo_springboot_tokonyadia.dto.response;

import java.util.UUID;

import lombok.Builder;

@Builder
public record ProductResponse(
    UUID id,
    String name,
    Double cogm,
    Integer stock) {

}
