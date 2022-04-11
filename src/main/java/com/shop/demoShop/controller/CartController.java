package com.shop.demoShop.controller;

import com.shop.demoShop.dto.CartItemDto;
import com.shop.demoShop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import com.shop.demoShop.dto.CartDetailDto;
import org.springframework.ui.Model;

@Controller
@RequiredArgsConstructor
public class CartController {

  private final CartService cartService;

  @PostMapping(value = "/cart")
  public @ResponseBody
  ResponseEntity order(@RequestBody @Valid CartItemDto cartItemDto, BindingResult bindingResult, Principal principal) {

    if (bindingResult.hasErrors()) { //cartItemDto 데이터 바인딩 시 에러 검사
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
    } catch (Exception e) {
      return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<Long>(cartItemId, HttpStatus.OK); // 결과값으로 생성된 장바구니 상품 아이디와 요청 성공 HTTP 응답을 반환
  }

  @GetMapping(value = "/cart")
  public String orderHist(Principal principal, Model model) {
    List<CartDetailDto> cartDetailList = cartService.getCartList(principal.getName());
    model.addAttribute("cartItems", cartDetailList);
    return "cart/cartList";
  }

  @PatchMapping(value = "/cartItem/{cartItemId}") // HTTP 메소드에서 PATCH는 요청된 자원의 일부를 업데이트할 때 PATCH를 사용
  public @ResponseBody
  ResponseEntity updateCartItem(@PathVariable("cartItemId") Long cartItemId, int count, Principal principal) {

    if (count <= 0) { // 장바구니에 담겨있는 상품의 개수를 0개 이하로 업데이트 요청을 할 때 에러 메시지를 담아서 반환
      return new ResponseEntity<String>("최소 1개 이상 담아주세요", HttpStatus.BAD_REQUEST);
    } else if (!cartService.validateCartItem(cartItemId, principal.getName())) { // 수정 권한을 체크
      return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
    }

    cartService.updateCartItemCount(cartItemId, count); // 장바구니 상품의 개수를 업데이트
    return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
  }

  @DeleteMapping(value = "/cartItem/{cartItemId}") // DeleteMapping 사용
  public @ResponseBody ResponseEntity deleteCartItem(@PathVariable("cartItemId") Long cartItemId, Principal principal){

    if(!cartService.validateCartItem(cartItemId, principal.getName())){ // 권한 체크
      return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
    }

    cartService.deleteCartItem(cartItemId); // 장바구니에서 상품 삭제

    return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
  }

}