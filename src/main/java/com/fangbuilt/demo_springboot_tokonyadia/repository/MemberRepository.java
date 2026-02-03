package com.fangbuilt.demo_springboot_tokonyadia.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fangbuilt.demo_springboot_tokonyadia.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {

}
