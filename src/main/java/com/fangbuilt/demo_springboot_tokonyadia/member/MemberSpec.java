package com.fangbuilt.demo_springboot_tokonyadia.member;

import org.springframework.data.jpa.domain.Specification;

/**
 * Specification buat filtering Member.
 * Useful buat admin dashboard atau user management.
 */
public class MemberSpec {

  /**
   * Filter by username (case-insensitive, partial match).
   */
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

  /**
   * Filter members yang punya customer profile.
   * Berguna buat distinguish antara "account only" vs "full profile"
   */
  public static Specification<MemberNtt> hasCustomerProfile() {
    return (root, query, cb) -> cb.isNotNull(root.get("customer"));
  }

  /**
   * Filter members yang belum punya customer profile.
   * Berguna buat "incomplete registration" tracking.
   */
  public static Specification<MemberNtt> hasNoCustomerProfile() {
    return (root, query, cb) -> cb.isNull(root.get("customer"));
  }

  /**
   * Exclude soft-deleted members.
   */
  public static Specification<MemberNtt> excludeDeleted() {
    return (root, query, cb) -> cb.isNull(root.get("deletedAt"));
  }
}
