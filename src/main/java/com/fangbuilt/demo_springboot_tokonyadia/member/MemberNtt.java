package com.fangbuilt.demo_springboot_tokonyadia.member;

import com.fangbuilt.demo_springboot_tokonyadia.common.ntt.BaseNtt;
import com.fangbuilt.demo_springboot_tokonyadia.customer.CustomerNtt;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Member = akun user yang bisa login.
 * One Member has one Customer profile (optional).
 * Soft delete: Iya (biar history login tetep ada).
 */
@Entity
@Table(name = "m_members")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberNtt extends BaseNtt {

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false)
    private String password; // Nanti di production harus di-hash (BCrypt)

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("member-customer")
    private CustomerNtt customer;
}
