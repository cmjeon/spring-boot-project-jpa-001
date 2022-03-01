package com.shop.demoShop.controller;

import com.shop.demoShop.dto.MemberFormDto;
import com.shop.demoShop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shop.demoShop.entity.Member;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.validation.BindingResult;
import javax.validation.Valid;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;
  private final PasswordEncoder passwordEncoder;

  @GetMapping(value="/new")
  public String memberForm(Model model) {
    model.addAttribute("memberFormDto", new MemberFormDto());
    return "member/memberForm";
  }

  @PostMapping(value = "/new")
  public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) { // 1
    if(bindingResult.hasErrors()) {
      return "member/memberForm";
    }
    try {
      Member member = Member.createMember(memberFormDto, passwordEncoder);
      memberService.saveMember(member);
    } catch (IllegalStateException e){
      model.addAttribute("errorMessage", e.getMessage());
      return "member/memberForm";
    }
    return "redirect:/";
  }
}