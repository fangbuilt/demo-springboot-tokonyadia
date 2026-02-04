package com.fangbuilt.demo_springboot_tokonyadia.customer;

import org.springframework.data.jpa.domain.Specification;

import com.fangbuilt.demo_springboot_tokonyadia.common.util.Gender;

public class CustomerSpec {
  public static Specification<CustomerNtt> hasFullnameLike(String fullname) {
    return (root, query, cb) -> {
      if (fullname == null || fullname.isBlank()) {
        return cb.conjunction();
      }
      return cb.like(
          cb.lower(root.get("fullname")),
          "%" + fullname.toLowerCase() + "%");
    };
  }

  public static Specification<CustomerNtt> hasEmailLike(String email) {
    return (root, query, cb) -> {
      if (email == null || email.isBlank()) {
        return cb.conjunction();
      }
      return cb.like(
          cb.lower(root.get("email")),
          "%" + email.toLowerCase() + "%");
    };
  }

  public static Specification<CustomerNtt> hasAddressLike(String address) {
    return (root, query, cb) -> {
      if (address == null || address.isBlank()) {
        return cb.conjunction();
      }
      return cb.like(
          cb.lower(root.get("address")),
          "%" + address.toLowerCase() + "%");
    };
  }

  public static Specification<CustomerNtt> hasGender(Gender gender) {
    return (root, query, cb) -> {
      if (gender == null) {
        return cb.conjunction();
      }
      return cb.equal(root.get("gender"), gender);
    };
  }

  public static Specification<CustomerNtt> excludeDeleted() {
    return (root, query, cb) -> cb.isNull(root.get("deletedAt"));
  }
}
