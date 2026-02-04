package com.fangbuilt.demo_springboot_tokonyadia.receipt;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptRepo extends JpaRepository<ReceiptNtt, UUID> {

}
