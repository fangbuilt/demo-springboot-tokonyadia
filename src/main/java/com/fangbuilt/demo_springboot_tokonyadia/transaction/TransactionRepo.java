package com.fangbuilt.demo_springboot_tokonyadia.transaction;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepo
    extends
    JpaRepository<TransactionNtt, UUID>,
    JpaSpecificationExecutor<TransactionNtt> {
}
