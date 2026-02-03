package com.fangbuilt.demo_springboot_tokonyadia.dto.response;

import java.util.UUID;

import lombok.Builder;

@Builder
public record MemberResponse(
    UUID id,
    String username) {

}
