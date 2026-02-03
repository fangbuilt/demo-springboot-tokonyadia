package com.fangbuilt.demo_springboot_tokonyadia.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fangbuilt.demo_springboot_tokonyadia.entity.Member;
import com.fangbuilt.demo_springboot_tokonyadia.repository.MemberRepository;

@Service
public class MemberServiceImpl implements MemberService {
  private final MemberRepository memberRepository;

  public MemberServiceImpl(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  @Override
  public Member create(Member member) {
    return memberRepository.save(member);
  }

  @Override
  public List<Member> read() {
    return memberRepository.findAll();
  }

  @Override
  public Member read(UUID id) {
    return memberRepository.findById(id).orElse(null);
  }

  @Override
  public Member update(UUID id, Member member) {
    Member existingMember = memberRepository.findById(id).orElse(null);
    if (existingMember == null) {
      return null;
    }
    existingMember.setUsername(member.getUsername());
    existingMember.setPassword(member.getPassword());
    return memberRepository.save(existingMember);
  }

  @Override
  public void delete(UUID id) {
    memberRepository.deleteById(id);
  }
}
