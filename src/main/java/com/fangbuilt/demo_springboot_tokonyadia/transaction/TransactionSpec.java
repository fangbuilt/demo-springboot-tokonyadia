package com.fangbuilt.demo_springboot_tokonyadia.transaction;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

public class TransactionSpec {
	public static Specification<TransactionNtt> hasCustomerId(UUID customerId) {
		return (root, query, cb) -> {
			if (customerId == null) {
				return cb.conjunction();
			}
			return cb.equal(root.get("customer").get("id"), customerId);
		};
	}

	public static Specification<TransactionNtt> hasCustomerNameLike(String customerName) {
		return (root, query, cb) -> {
			if (customerName == null || customerName.isBlank()) {
				return cb.conjunction();
			}
			return cb.like(
					cb.lower(root.get("customer").get("fullname")),
					"%" + customerName.toLowerCase() + "%");
		};
	}

	public static Specification<TransactionNtt> hasDateRange(LocalDateTime startDate, LocalDateTime endDate) {
		return (root, query, cb) -> {
			if (startDate == null && endDate == null) {
				return cb.conjunction();
			}
			if (startDate == null) {
				return cb.lessThanOrEqualTo(root.get("createdAt"), endDate);
			}
			if (endDate == null) {
				return cb.greaterThanOrEqualTo(root.get("createdAt"), startDate);
			}
			return cb.between(root.get("createdAt"), startDate, endDate);
		};
	}

	public static Specification<TransactionNtt> isToday() {
		LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
		LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
		return hasDateRange(startOfDay, endOfDay);
	}

	public static Specification<TransactionNtt> isThisWeek() {
		LocalDateTime startOfWeek = LocalDateTime.now().minusDays(7);
		return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), startOfWeek);
	}

	public static Specification<TransactionNtt> isThisMonth() {
		LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
		return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), startOfMonth);
	}
}
