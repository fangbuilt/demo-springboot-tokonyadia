package com.fangbuilt.demo_springboot_tokonyadia.service;

import java.util.List;
import java.util.UUID;

import com.fangbuilt.demo_springboot_tokonyadia.entity.Product;

public interface ProductService {
  Product create(Product product);
  List<Product> read();
  Product read(UUID id);
  Product update(UUID id, Product product);
  void delete(UUID id);
}
