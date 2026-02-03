package com.fangbuilt.demo_springboot_tokonyadia.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fangbuilt.demo_springboot_tokonyadia.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

}
