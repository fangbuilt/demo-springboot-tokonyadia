package com.fangbuilt.demo_springboot_tokonyadia.member;

import com.fangbuilt.demo_springboot_tokonyadia.common.ntt.BaseNtt;
import com.fangbuilt.demo_springboot_tokonyadia.customer.CustomerNtt;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String password; // BCrypt hashed

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private MemberRole role = MemberRole.USER; // Default role USER

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true; // Buat disable account (separate dari soft delete)

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("member-customer")
    private CustomerNtt customer;

    public enum MemberRole {
        USER, ADMIN
    }
}
