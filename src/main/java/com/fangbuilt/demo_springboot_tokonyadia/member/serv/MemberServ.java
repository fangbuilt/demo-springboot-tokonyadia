package com.fangbuilt.demo_springboot_tokonyadia.member.serv;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fangbuilt.demo_springboot_tokonyadia.member.dto.MemberReq;
import com.fangbuilt.demo_springboot_tokonyadia.member.dto.MemberRes;

public interface MemberServ {
  MemberRes create(MemberReq request);

  MemberRes read(UUID id);

  Page<MemberRes> read(String username, Boolean hasCustomerProfile, Pageable pageable);

  MemberRes update(UUID id, MemberReq request);

  void delete(UUID id); // Soft delete
}
