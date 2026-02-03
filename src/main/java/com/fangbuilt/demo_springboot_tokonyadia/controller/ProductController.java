package com.fangbuilt.demo_springboot_tokonyadia.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fangbuilt.demo_springboot_tokonyadia.entity.Product;
import com.fangbuilt.demo_springboot_tokonyadia.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {
  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @PostMapping
  public Product create(@RequestBody Product product) {
    return productService.create(product);
  }

  @GetMapping
  public List<Product> read() {
    return productService.read();
  }

  @GetMapping("/{id}")
  public Product read(@PathVariable UUID id) {
    return productService.read(id);
  }

  @PutMapping("/{id}")
  public Product update(@PathVariable UUID id, @RequestBody Product product) {
    return productService.update(id, product);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable UUID id) {
    productService.delete(id);
  }
}
