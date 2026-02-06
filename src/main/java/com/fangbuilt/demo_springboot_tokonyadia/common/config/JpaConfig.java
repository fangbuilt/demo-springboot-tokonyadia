package com.fangbuilt.demo_springboot_tokonyadia.common.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaConfig {
  @Bean
  public AuditorAware<String> auditorAware() {
    return () -> Optional.of("SYSTEM");
  } // AuditorAware ini biar kita bisa lihat "siapa" juga,
    // Bukan "kapan" doang
}
