package com.fangbuilt.demo_springboot_tokonyadia.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fangbuilt.demo_springboot_tokonyadia.entity.Product;
import com.fangbuilt.demo_springboot_tokonyadia.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {
  private final ProductRepository productRepository;

  public ProductServiceImpl(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Override
  public Product create(Product product) {
    return productRepository.save(product);
  }

  @Override
  public List<Product> read() {
    return productRepository.findAll();
  }

  @Override
  public Product read(UUID id) {
    return productRepository.findById(id).orElse(null);
  }

  @Override
  public Product update(UUID id, Product product) {
    Product existingProduct = productRepository.findById(id).orElse(null);
    if (existingProduct == null) {
      return null;
    }
    existingProduct.setName(product.getName());
    existingProduct.setCogm(product.getCogm());
    existingProduct.setStock(product.getStock());
    return productRepository.save(existingProduct);
  }

  @Override
  public void delete(UUID id) {
    productRepository.deleteById(id);
  }
}
