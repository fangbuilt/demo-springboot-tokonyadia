package com.fangbuilt.demo_springboot_tokonyadia.product;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fangbuilt.demo_springboot_tokonyadia.product.dto.ProductReq;
import com.fangbuilt.demo_springboot_tokonyadia.product.dto.ProductRes;
import com.fangbuilt.demo_springboot_tokonyadia.product.serv.ProductServ;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller untuk Product endpoints dengan powerful filtering.
 */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductCtrl {

  private final ProductServ productServ;

  @PostMapping
  public ResponseEntity<ProductRes> create(@Valid
  @RequestBody
  ProductReq payload) {
    ProductRes response = productServ.create(payload);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductRes> read(@PathVariable
  UUID id) {
    return ResponseEntity.ok(productServ.read(id));
  }

  /**
   * Get products dengan pagination dan filtering yang lengkap.
   *
   * Query params:
   * - name: Filter by product name (partial match)
   * - minPrice, maxPrice: Filter by price range
   * - minStock, maxStock: Filter by stock range
   * - available: Filter produk yang ready stock (stok > 0)
   * - page, size, sort: Pagination params
   *
   * Examples:
   * 1. Search laptop: GET /products?name=laptop&page=0&size=10
   * 2. Price range 1-5 juta: GET /products?minPrice=1000000&maxPrice=5000000
   * 3. Ready stock only: GET /products?available=true
   * 4. Combine: GET
   * /products?name=laptop&minPrice=1000000&available=true&sort=cogm,asc
   */
  @GetMapping
  public ResponseEntity<Page<ProductRes>> read(
      @RequestParam(required = false)
      String name,
      @RequestParam(required = false)
      Double minPrice,
      @RequestParam(required = false)
      Double maxPrice,
      @RequestParam(required = false)
      Integer minStock,
      @RequestParam(required = false)
      Integer maxStock,
      @RequestParam(required = false)
      Boolean available,
      Pageable pageable) {
    return ResponseEntity.ok(
        productServ.read(name, minPrice, maxPrice, minStock, maxStock, available, pageable));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProductRes> update(
      @PathVariable
      UUID id,
      @Valid
      @RequestBody
      ProductReq payload) {
    return ResponseEntity.ok(productServ.update(id, payload));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable
  UUID id) {
    productServ.delete(id);
    return ResponseEntity.noContent().build();
  }
}
