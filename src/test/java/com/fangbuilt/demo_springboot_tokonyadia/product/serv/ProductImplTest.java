package com.fangbuilt.demo_springboot_tokonyadia.product.serv;

import com.fangbuilt.demo_springboot_tokonyadia.product.ProductNtt;
import com.fangbuilt.demo_springboot_tokonyadia.product.ProductRepo;
import com.fangbuilt.demo_springboot_tokonyadia.product.dto.ProductReq;
import com.fangbuilt.demo_springboot_tokonyadia.product.dto.ProductRes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductImplTest {

  @Mock
  private ProductRepo productRepo;

  @InjectMocks
  private ProductImpl productImpl;

  @Test
  void create_shouldReturnProductRes() {
    ProductReq request = new ProductReq("laptop", 2000000.00, 9);

    ProductNtt savedProduct = ProductNtt.builder()
        .id(UUID.randomUUID())
        .name("laptop")
        .cogm(2000000.00)
        .stock(9)
        .build();

    when(productRepo.save(any(ProductNtt.class))).thenReturn(savedProduct);

    ProductRes result = productImpl.create(request);

    assertThat(result).isNotNull();
    assertThat(result.id()).isNotNull(); // ID harus ada
    assertThat(result.name()).isEqualTo("laptop");

    verify(productRepo, times(1)).save(any(ProductNtt.class));
  }

  @Test
  void readAll_shouldReturnPageOfProductRes() {
    Pageable pageable = PageRequest.of(0, 10);
    ProductNtt product = ProductNtt.builder()
        .id(UUID.randomUUID())
        .name("laptop")
        .cogm(2000000.00)
        .stock(9)
        .build();

    Page<ProductNtt> productPage = new PageImpl<>(List.of(product), pageable, 1);

    when(productRepo.findAll(any(Specification.class), eq(pageable))).thenReturn(productPage);

    Page<ProductRes> result = productImpl.read(null, null, null, null, null, null, pageable);

    assertThat(result).isNotNull();
    assertThat(result.getContent()).hasSize(1);
    assertThat(result.getContent().get(0).name()).isEqualTo("laptop");
  }

  @Test
  void readOne_shouldReturnProductRes() {
    // Given
    UUID id = UUID.randomUUID();
    ProductNtt product = ProductNtt.builder()
        .id(id)
        .name("HP Omen")
        .build();

    // When green
    when(productRepo.findById(id)).thenReturn(Optional.of(product));

    // Action
    ProductRes result = productImpl.read(id);

    // Then
    assertThat(result.id()).isEqualTo(id);
    assertThat(result.name()).isEqualTo("HP Omen");
  }

  @Test
  void readOne_notFound_shouldThrowException() {
    // Given
    UUID randomId = UUID.randomUUID();

    // When
    when(productRepo.findById(randomId)).thenReturn(Optional.empty());

    // Then
    assertThrows(ResponseStatusException.class, () -> {
      productImpl.read(randomId);
    });
  }

  @Test
  void update_shouldReturnUpdatedProductRes() {
    // Given
    UUID id = UUID.randomUUID();
    ProductReq updateReq = new ProductReq("Macbook", 30000000.00, 5);

    ProductNtt existingProduct = ProductNtt.builder().id(id).name("Laptop Lama").build();
    ProductNtt updatedProduct = ProductNtt.builder().id(id).name("Macbook").build();

    // When
    when(productRepo.findById(id)).thenReturn(Optional.of(existingProduct));
    when(productRepo.save(any(ProductNtt.class))).thenReturn(updatedProduct);

    // Action
    ProductRes result = productImpl.update(id, updateReq);

    // Then
    assertThat(result.name()).isEqualTo("Macbook");

    verify(productRepo, times(1)).save(any(ProductNtt.class));
  }

  @Test
  void delete_shouldSetDeletedAt() {
    // Given
    UUID id = UUID.randomUUID();
    ProductNtt product = ProductNtt.builder().id(id).build();

    // When
    when(productRepo.findById(id)).thenReturn(Optional.of(product));

    // Action
    productImpl.delete(id);

    // Then
    // Kita cek apakah repo.save dipanggil.
    // Kenapa save? Karena soft delete itu update kolom deletedAt, bukan delete row.
    verify(productRepo, times(1)).save(any(ProductNtt.class));
  }
}