package com.fangbuilt.demo_springboot_tokonyadia.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fangbuilt.demo_springboot_tokonyadia.entity.Member;
import com.fangbuilt.demo_springboot_tokonyadia.service.MemberService;

@RestController
@RequestMapping("/members")
public class MemberController {
  private final MemberService memberService;

  public MemberController(MemberService memberService) {
    this.memberService = memberService;
  }

  @PostMapping
  public Member create(@RequestBody Member member) {
    return memberService.create(member);
  }

  @GetMapping
  public List<Member> read() {
    return memberService.read();
  }

  @GetMapping("/{id}")
  public Member read(@PathVariable UUID id) {
    return memberService.read(id);
  }

  @PutMapping("/{id}")
  public Member update(@PathVariable UUID id, @RequestBody Member member) {
    return memberService.update(id, member);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable UUID id) {
    memberService.delete(id);
  }
}
