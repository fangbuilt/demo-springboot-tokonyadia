package com.fangbuilt.demo_springboot_tokonyadia.customer;

import java.util.List;

import com.fangbuilt.demo_springboot_tokonyadia.common.ntt.BaseNtt;
import com.fangbuilt.demo_springboot_tokonyadia.common.util.Gender;
import com.fangbuilt.demo_springboot_tokonyadia.transaction.TransactionNtt;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Customer = profil pembeli yang bisa punya Member account (atau guest).
 * One Customer has many Transactions.
 * Soft delete: Iya (biar history transaksi tetep lengkap).
 */
@Entity
@Table(name = "m_customers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerNtt extends BaseNtt {

    @Column(nullable = false, length = 200)
    private String fullname;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(length = 500)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;

    @OneToOne
    @JoinColumn(name = "member_id", unique = true)
    @JsonBackReference("member-customer")
    private CustomerNtt member;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("customer-transactions")
    private List<TransactionNtt> transactions;
}
