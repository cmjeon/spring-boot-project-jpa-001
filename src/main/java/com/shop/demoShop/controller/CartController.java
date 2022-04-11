package com.shop.demoShop.controller;

import com.shop.demoShop.dto.CartItemDto;
import com.shop.demoShop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

  private final CartService cartService;

  @PostMapping(value = "/cart")
  public @ResponseBody ResponseEntity order(@RequestBody @Valid CartItemDto cartItemDto, BindingResult bindingResult, Principal principal){

    if(bindingResult.hasErrors()){ //cartItemDto 데이터 바인딩 시 에러 검사
      StringBuilder sb = new StringBuilder();
      List<FieldError> fieldErrors = bindingResult.getFieldErrors();

      for (FieldError fieldError : fieldErrors) {
        sb.append(fieldError.getDefaultMessage());
      }

      return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
    }

    String email = principal.getName(); // 이메일 정보를 변수에 저장
    Long cartItemId;

    try {
      cartItemId = cartService.addCart(cartItemDto, email); // 상품정보과 로그인한 회원 이메일 정보를 이용하여 장바구니에 상품을 담는 로직을 호출
    } catch(Exception e){
      return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<Long>(cartItemId, HttpStatus.OK); // 결과값으로 생성된 장바구니 상품 아이디와 요청 성공 HTTP 응답을 반환
  }

}