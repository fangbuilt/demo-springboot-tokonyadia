package com.fangbuilt.demo_springboot_tokonyadia.member;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository untuk Member.
 * JpaSpecificationExecutor biar bisa pakai dynamic filtering dengan Specification.
 */
@Repository
public interface MemberRepo extends JpaRepository<MemberNtt, UUID>, JpaSpecificationExecutor<MemberNtt> {
    // Spring Data JPA akan auto-generate method ini
    boolean existsByUsername(String username);
}
