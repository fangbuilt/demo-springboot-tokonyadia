package com.fangbuilt.demo_springboot_tokonyadia.dto.request;

import java.util.UUID;

import com.fangbuilt.demo_springboot_tokonyadia.utility.Gender;

public record CustomerRequest(
    String fullname,
    String email,
    String address,
    Gender gender,
    UUID memberId) {
}