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

/**
 * ProductService dengan soft delete support dan comprehensive filtering.
 *
 * Soft Delete Pattern:
 * - Create/Update: Normal operation
 * - Delete: Set deletedAt = now(), data masih ada di DB
 * - Read: Secara default exclude deleted items (filter deletedAt = null)
 */
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

    // Kalau product di-soft delete, throw 404
    if (product.getDeletedAt() != null) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "Produk dengan ID " + id + " tidak ditemukan");
    }

    return toResponse(product);
  }

  /**
   * Read dengan filtering yang powerful.
   *
   * Example queries yang bisa dilakukan:
   * 1. Search by name: ?name=laptop
   * 2. Price range: ?minPrice=1000000&maxPrice=5000000
   * 3. Stock range: ?minStock=10&maxStock=100
   * 4. Available only: ?available=true
   * 5. Combine semua: ?name=laptop&minPrice=1000000&available=true
   */
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
    // Start dengan spec yang exclude deleted items (default behavior)
    Specification<ProductNtt> spec = ProductSpec.excludeDeleted();

    // Build dynamic query berdasarkan filter yang ada
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

    // Kalau product di-soft delete, gak bisa di-update
    if (product.getDeletedAt() != null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Tidak bisa update produk yang sudah dihapus");
    }

    // Update fields
    product.setName(payload.name());
    product.setCogm(payload.cogm()); // Harga bisa berubah di sini, tapi receipt tetep pakai COGS lama
    product.setStock(payload.stock());

    return toResponse(productRepo.save(product));
  }

  /**
   * SOFT DELETE implementation.
   *
   * Kenapa soft delete untuk product?
   * - Product masih referenced di receipts (historical transactions)
   * - Kalau hard delete, foreign key constraint error atau data loss
   * - Dengan soft delete, product tetep ada buat historical data,
   * tapi gak muncul di product list untuk pembelian baru
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(UUID id) {
    ProductNtt product = productRepo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Produk dengan ID " + id + " tidak ditemukan"));

    // Kalau udah di-delete sebelumnya, skip
    if (product.getDeletedAt() != null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Produk sudah dihapus sebelumnya");
    }

    // Set deletedAt = now (SOFT DELETE)
    product.setDeletedAt(LocalDateTime.now());
    productRepo.save(product);
  }

  /**
   * Helper method convert Entity → Response DTO.
   */
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
