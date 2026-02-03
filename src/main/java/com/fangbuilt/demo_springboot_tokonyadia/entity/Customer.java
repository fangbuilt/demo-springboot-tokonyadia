package com.fangbuilt.demo_springboot_tokonyadia.entity;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.fangbuilt.demo_springboot_tokonyadia.utility.Gender;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "m_customers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {
  @Id
  @GeneratedValue
  @UuidGenerator
  private UUID id;

  private String fullname;
  private String email;
  private String address;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  @OneToOne
  @JoinColumn(name = "member_id", unique = true)
  @JsonBackReference("member-customer")
  private Member member;

  @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference("customer-transactions")
  private List<Transaction> transactions;
}