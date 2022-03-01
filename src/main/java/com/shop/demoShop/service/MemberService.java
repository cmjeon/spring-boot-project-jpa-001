package com.shop.demoShop.service;

import com.shop.demoShop.entity.Member;
import com.shop.demoShop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional // 1
@RequiredArgsConstructor // 2
public class MemberService {

  private final MemberRepository memberRepository; // 3

  public Member saveMember(Member member) {
    validateDuplicateMember(member);
    return memberRepository.save(member);
  }

  private void validateDuplicateMember(Member member) { // 4
    Member findMember = memberRepository.findByEmail(member.getEmail());
    if(findMember != null) {
      throw new IllegalStateException("이미 가입된 회원입니다.");
    }
  }
}