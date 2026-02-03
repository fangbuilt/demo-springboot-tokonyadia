package com.fangbuilt.demo_springboot_tokonyadia.product.serv;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fangbuilt.demo_springboot_tokonyadia.product.dto.ProductReq;
import com.fangbuilt.demo_springboot_tokonyadia.product.dto.ProductRes;

public interface ProductServ {
  ProductRes create(ProductReq payload);

  ProductRes read(UUID id);

  Page<ProductRes> read(String name, Double minPrice, Double maxPrice, Integer minStock, Integer maxStock,
      Boolean available, Pageable pageable);

  ProductRes update(UUID id, ProductReq payload);

  void delete(UUID id); // Soft delete
}
