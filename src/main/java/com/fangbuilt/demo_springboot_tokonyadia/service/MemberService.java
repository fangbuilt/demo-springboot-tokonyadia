package com.fangbuilt.demo_springboot_tokonyadia.service;

import java.util.List;
import java.util.UUID;

import com.fangbuilt.demo_springboot_tokonyadia.entity.Member;

public interface MemberService {
  Member create(Member member);
  List<Member> read();
  Member read(UUID id);
  Member update(UUID id, Member member);
  void delete(UUID id);
}
