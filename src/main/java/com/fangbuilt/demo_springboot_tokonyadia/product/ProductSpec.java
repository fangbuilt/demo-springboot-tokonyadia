package com.fangbuilt.demo_springboot_tokonyadia.product;

import org.springframework.data.jpa.domain.Specification;

public class ProductSpec {
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

  public static Specification<ProductNtt> hasMinPrice(Double minPrice) {
    return (root, query, cb) -> {
      if (minPrice == null) {
        return cb.conjunction();
      }
      return cb.greaterThanOrEqualTo(root.get("cogm"), minPrice);
    };
  }

  public static Specification<ProductNtt> hasMaxPrice(Double maxPrice) {
    return (root, query, cb) -> {
      if (maxPrice == null) {
        return cb.conjunction();
      }
      return cb.lessThanOrEqualTo(root.get("cogm"), maxPrice);
    };
  }

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

  public static Specification<ProductNtt> hasMinStock(Integer minStock) {
    return (root, query, cb) -> {
      if (minStock == null) {
        return cb.conjunction();
      }
      return cb.greaterThanOrEqualTo(root.get("stock"), minStock);
    };
  }

  public static Specification<ProductNtt> hasMaxStock(Integer maxStock) {
    return (root, query, cb) -> {
      if (maxStock == null) {
        return cb.conjunction();
      }
      return cb.lessThanOrEqualTo(root.get("stock"), maxStock);
    };
  }

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

  public static Specification<ProductNtt> isAvailable() {
    return (root, query, cb) -> cb.greaterThan(root.get("stock"), 0);
  }

  public static Specification<ProductNtt> isOutOfStock() {
    return (root, query, cb) -> cb.equal(root.get("stock"), 0);
  }

  public static Specification<ProductNtt> excludeDeleted() {
    return (root, query, cb) -> cb.isNull(root.get("deletedAt"));
  }
}