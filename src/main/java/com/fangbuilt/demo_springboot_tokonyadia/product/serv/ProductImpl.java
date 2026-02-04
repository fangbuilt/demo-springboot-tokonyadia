package com.fangbuilt.demo_springboot_tokonyadia.product.serv;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.fangbuilt.demo_springboot_tokonyadia.product.ProductNtt;
import com.fangbuilt.demo_springboot_tokonyadia.product.ProductRepo;
import com.fangbuilt.demo_springboot_tokonyadia.product.ProductSpec;
import com.fangbuilt.demo_springboot_tokonyadia.product.dto.ProductReq;
import com.fangbuilt.demo_springboot_tokonyadia.product.dto.ProductRes;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductImpl implements ProductServ {

  private final ProductRepo productRepo;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ProductRes create(ProductReq payload) {
    ProductNtt product = ProductNtt.builder()
        .name(payload.name())
        .cogm(payload.cogm())
        .stock(payload.stock())
        .build();

    return toResponse(productRepo.save(product));
  }

  @Override
  @Transactional(readOnly = true)
  public ProductRes read(UUID id) {
    ProductNtt product = productRepo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Produk dengan ID " + id + " tidak ditemukan"));

    if (product.getDeletedAt() != null) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "Produk dengan ID " + id + " tidak ditemukan");
    }

    return toResponse(product);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ProductRes> read(
      String name,
      Double minPrice,
      Double maxPrice,
      Integer minStock,
      Integer maxStock,
      Boolean available,
      Pageable pageable) {
    Specification<ProductNtt> spec = ProductSpec.excludeDeleted();

    if (name != null && !name.isBlank()) {
      spec = spec.and(ProductSpec.hasNameLike(name));
    }
    if (minPrice != null || maxPrice != null) {
      spec = spec.and(ProductSpec.hasPriceRange(minPrice, maxPrice));
    }
    if (minStock != null || maxStock != null) {
      spec = spec.and(ProductSpec.hasStockRange(minStock, maxStock));
    }
    if (available != null) {
      if (available) {
        spec = spec.and(ProductSpec.isAvailable());
      } else {
        spec = spec.and(ProductSpec.isOutOfStock());
      }
    }

    return productRepo.findAll(spec, pageable).map(this::toResponse);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ProductRes update(UUID id, ProductReq payload) {
    ProductNtt product = productRepo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Produk dengan ID " + id + " tidak ditemukan"));

    if (product.getDeletedAt() != null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Tidak bisa update produk yang sudah dihapus");
    }

    product.setName(payload.name());
    product.setCogm(payload.cogm()); // Snapshotted as COGS on Receipt
    product.setStock(payload.stock());

    return toResponse(productRepo.save(product));
  }

 // Audit and casdade concern: Selain soft delete, bisa di snapshot juga di receipt
  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(UUID id) {
    ProductNtt product = productRepo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Produk dengan ID " + id + " tidak ditemukan"));

    if (product.getDeletedAt() != null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Produk sudah dihapus sebelumnya");
    }

    product.setDeletedAt(LocalDateTime.now());
    productRepo.save(product);
  }

  private ProductRes toResponse(ProductNtt product) {
    return ProductRes.builder()
        .id(product.getId())
        .name(product.getName())
        .cogm(product.getCogm())
        .stock(product.getStock())
        .createdAt(product.getCreatedAt())
        .updatedAt(product.getUpdatedAt())
        .build();
  }
}
