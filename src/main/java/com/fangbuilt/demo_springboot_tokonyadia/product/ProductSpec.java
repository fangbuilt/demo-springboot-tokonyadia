package com.fangbuilt.demo_springboot_tokonyadia.product;

import org.springframework.data.jpa.domain.Specification;

/**
 * Specification buat filtering Product dengan berbagai kriteria.
 *
 * Kenapa pakai Specification?
 * - Dynamic query: Bisa combine filter sesuai kebutuhan (AND/OR)
 * - Type-safe: Compile-time check, gak typo field name
 * - Reusable: Spec bisa di-combine dan dipake ulang
 *
 * Contoh usage:
 * Spec<Product> spec = hasNameLike("laptop")
 * .and(hasPriceRange(1000000.0, 5000000.0))
 * .and(hasMinStock(10));
 */
public class ProductSpec {

  /**
   * Filter by nama produk (case-insensitive, partial match).
   * Example: "laptop" akan match "Gaming Laptop", "Laptop Dell", etc.
   */
  public static Specification<ProductNtt> hasNameLike(String name) {
    return (root, query, cb) -> {
      if (name == null || name.isBlank()) {
        return cb.conjunction(); // Return true (gak filter apa-apa)
      }
      return cb.like(
          cb.lower(root.get("name")),
          "%" + name.toLowerCase() + "%");
    };
  }

  /**
   * Filter by minimum price (COGM).
   * Example: minPrice=100000 → tampil semua produk >= 100.000
   */
  public static Specification<ProductNtt> hasMinPrice(Double minPrice) {
    return (root, query, cb) -> {
      if (minPrice == null) {
        return cb.conjunction();
      }
      return cb.greaterThanOrEqualTo(root.get("cogm"), minPrice);
    };
  }

  /**
   * Filter by maximum price (COGM).
   * Example: maxPrice=500000 → tampil semua produk <= 500.000
   */
  public static Specification<ProductNtt> hasMaxPrice(Double maxPrice) {
    return (root, query, cb) -> {
      if (maxPrice == null) {
        return cb.conjunction();
      }
      return cb.lessThanOrEqualTo(root.get("cogm"), maxPrice);
    };
  }

  /**
   * Filter by price range (min dan max sekaligus).
   * Example: range 100k-500k → tampil produk dalam range itu.
   */
  public static Specification<ProductNtt> hasPriceRange(Double minPrice, Double maxPrice) {
    return (root, query, cb) -> {
      if (minPrice == null && maxPrice == null) {
        return cb.conjunction();
      }
      if (minPrice == null) {
        return cb.lessThanOrEqualTo(root.get("cogm"), maxPrice);
      }
      if (maxPrice == null) {
        return cb.greaterThanOrEqualTo(root.get("cogm"), minPrice);
      }
      return cb.between(root.get("cogm"), minPrice, maxPrice);
    };
  }

  /**
   * Filter by minimum stock.
   * Example: minStock=10 → tampil produk dengan stok >= 10
   * Berguna buat filter "ready stock" atau "bulk order available"
   */
  public static Specification<ProductNtt> hasMinStock(Integer minStock) {
    return (root, query, cb) -> {
      if (minStock == null) {
        return cb.conjunction();
      }
      return cb.greaterThanOrEqualTo(root.get("stock"), minStock);
    };
  }

  /**
   * Filter by maximum stock.
   * Example: maxStock=5 → tampil produk dengan stok <= 5
   * Berguna buat filter "limited stock" atau "clearance sale"
   */
  public static Specification<ProductNtt> hasMaxStock(Integer maxStock) {
    return (root, query, cb) -> {
      if (maxStock == null) {
        return cb.conjunction();
      }
      return cb.lessThanOrEqualTo(root.get("stock"), maxStock);
    };
  }

  /**
   * Filter by stock range.
   */
  public static Specification<ProductNtt> hasStockRange(Integer minStock, Integer maxStock) {
    return (root, query, cb) -> {
      if (minStock == null && maxStock == null) {
        return cb.conjunction();
      }
      if (minStock == null) {
        return cb.lessThanOrEqualTo(root.get("stock"), maxStock);
      }
      if (maxStock == null) {
        return cb.greaterThanOrEqualTo(root.get("stock"), minStock);
      }
      return cb.between(root.get("stock"), minStock, maxStock);
    };
  }

  /**
   * Filter produk yang available (stok > 0).
   * Shortcut buat "in stock" filter.
   */
  public static Specification<ProductNtt> isAvailable() {
    return (root, query, cb) -> cb.greaterThan(root.get("stock"), 0);
  }

  /**
   * Filter produk yang out of stock (stok = 0).
   * Berguna buat inventory management.
   */
  public static Specification<ProductNtt> isOutOfStock() {
    return (root, query, cb) -> cb.equal(root.get("stock"), 0);
  }

  /**
   * Filter by soft delete status.
   * excludeDeleted=true → hanya tampil produk yang aktif (deletedAt = null)
   */
  public static Specification<ProductNtt> excludeDeleted() {
    return (root, query, cb) -> cb.isNull(root.get("deletedAt"));
  }
}