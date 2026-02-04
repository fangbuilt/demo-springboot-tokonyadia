package com.fangbuilt.demo_springboot_tokonyadia.member;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepo extends JpaRepository<MemberNtt, UUID>, JpaSpecificationExecutor<MemberNtt> {

    // Dipakai pas login dan validasi JWT
    Optional<MemberNtt> findByUsername(String username);

    /**
     * Cek apakah username sudah ada
     * Lebih efisien daripada findByUsername kalau cuma perlu cek existence
     */
    Boolean existsByUsername(String username);
}
