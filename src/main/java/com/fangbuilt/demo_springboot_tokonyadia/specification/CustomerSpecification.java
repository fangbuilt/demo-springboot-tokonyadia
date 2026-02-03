package com.fangbuilt.demo_springboot_tokonyadia.specification;

import org.springframework.data.jpa.domain.Specification;

import com.fangbuilt.demo_springboot_tokonyadia.entity.Customer;

public class CustomerSpecification {
  public static Specification<Customer> hasFullnameLike(String fullname) {
    return (root, query, criteriaBuilder) -> {
      if (fullname == null || fullname.isBlank()) {
        return criteriaBuilder.conjunction(); // Return "always true" (gak filter apa-apa)
      }
      return criteriaBuilder.like(
          criteriaBuilder.lower(root.get("fullname")),
          "%" + fullname.toLowerCase() + "%");
    };
  }
  public static Specification<Customer> hasEmailLike(String email) {
    return (root, query, criteriaBuilder) -> {
      if (email == null || email.isBlank()) {
        return criteriaBuilder.conjunction();
      }
      return criteriaBuilder.like(
          criteriaBuilder.lower(root.get("email")),
          "%" + email.toLowerCase() + "%");
    };
  }
}
