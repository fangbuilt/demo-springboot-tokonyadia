package com.fangbuilt.demo_springboot_tokonyadia.product;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo
    extends
    JpaRepository<ProductNtt, UUID>,
    JpaSpecificationExecutor<ProductNtt> {
}
