package com.fangbuilt.demo_springboot_tokonyadia.member;

import org.springframework.data.jpa.domain.Specification;

import com.fangbuilt.demo_springboot_tokonyadia.customer.CustomerNtt;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

public class MemberSpec {
  public static Specification<MemberNtt> hasUsernameLike(String username) {
    return (root, query, cb) -> {
      if (username == null || username.isBlank()) {
        return cb.conjunction();
      }
      return cb.like(
          cb.lower(root.get("username")),
          "%" + username.toLowerCase() + "%");
    };
  }

  public static Specification<MemberNtt> hasCustomerProfile() {
    return (root, query, cb) -> {
      Join<MemberNtt, CustomerNtt> customerJoin = root.join("customer", JoinType.INNER);
      return cb.and(
          cb.isNotNull(customerJoin),
          cb.isNull(customerJoin.get("deletedAt")));
    };
  }

  public static Specification<MemberNtt> hasNoCustomerProfile() {
    return (root, query, cb) -> {
      Join<MemberNtt, CustomerNtt> customerJoin = root.join("customer", JoinType.LEFT);
      return cb.or(
          cb.isNull(customerJoin),
          cb.isNotNull(customerJoin.get("deletedAt")));
    };
  }

  public static Specification<MemberNtt> excludeDeleted() {
    return (root, query, cb) -> cb.isNull(root.get("deletedAt"));
  }
}
