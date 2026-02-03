package com.fangbuilt.demo_springboot_tokonyadia.customer;

import org.springframework.data.jpa.domain.Specification;

import com.fangbuilt.demo_springboot_tokonyadia.common.util.Gender;

/**
 * Specification buat filtering Customer.
 * Extended version dengan lebih banyak filter options.
 */
public class CustomerSpec {

  /**
   * Filter by fullname (case-insensitive, partial match).
   */
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

  /**
   * Filter by email (case-insensitive, partial match).
   */
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

  /**
   * Filter by address (case-insensitive, partial match).
   * Berguna buat "pelanggan di area Jakarta" misalnya.
   */
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

  /**
   * Filter by gender.
   */
  public static Specification<CustomerNtt> hasGender(Gender gender) {
    return (root, query, cb) -> {
      if (gender == null) {
        return cb.conjunction();
      }
      return cb.equal(root.get("gender"), gender);
    };
  }

  /**
   * Filter customers yang punya member account.
   * Berguna buat distinguish "registered user" vs "guest checkout"
   */
  public static Specification<CustomerNtt> hasMemberAccount() {
    return (root, query, cb) -> cb.isNotNull(root.get("member"));
  }

  /**
   * Filter customers yang checkout sebagai guest (no member account).
   */
  public static Specification<CustomerNtt> isGuest() {
    return (root, query, cb) -> cb.isNull(root.get("member"));
  }

  /**
   * Exclude soft-deleted customers.
   */
  public static Specification<CustomerNtt> excludeDeleted() {
    return (root, query, cb) -> cb.isNull(root.get("deletedAt"));
  }
}
