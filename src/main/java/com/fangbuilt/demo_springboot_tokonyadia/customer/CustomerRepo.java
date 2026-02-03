package com.fangbuilt.demo_springboot_tokonyadia.customer;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository untuk Customer dengan specification support buat dynamic
 * filtering.
 */
@Repository
public interface CustomerRepo extends JpaRepository<CustomerNtt, UUID>, JpaSpecificationExecutor<CustomerNtt> {
}
