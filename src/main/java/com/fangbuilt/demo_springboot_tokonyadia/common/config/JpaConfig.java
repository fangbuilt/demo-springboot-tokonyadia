package com.fangbuilt.demo_springboot_tokonyadia.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Config buat enable JPA Auditing.
 * Ini yang bikin @CreatedDate dan @LastModifiedDate work otomatis.
 *
 * Tanpa ini, createdAt dan updatedAt gak bakal ke-set otomatis.
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}
