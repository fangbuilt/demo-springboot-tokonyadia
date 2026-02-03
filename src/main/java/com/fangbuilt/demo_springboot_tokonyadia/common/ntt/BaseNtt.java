package com.fangbuilt.demo_springboot_tokonyadia.common.ntt;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

/**
 * Base entity yang semua entity bakal extend dari sini.
 * Jadi gak perlu nulis UUID, createdAt, updatedAt, deletedAt berkali-kali.
 *
 * Soft delete: Kalau deletedAt != null, berarti data udah "dihapus" (tapi masih
 * ada di DB).
 * Kalau suatu entity butuh soft delete, tinggal set deletedAt aja.
 * Kalau gak butuh? Ya diabaikan aja field-nya. Simple!
 */
@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseNtt {

  @Id
  @GeneratedValue
  @UuidGenerator
  private UUID id;

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(nullable = false)
  private LocalDateTime updatedAt;

  /**
   * Soft delete field - kalau ini gak null, berarti data udah "dihapus"
   * Tapi masih ada di database buat audit trail atau recover nanti
   */
  @Column
  private LocalDateTime deletedAt;
}
