package com.shop.demoShop.service;

import com.shop.demoShop.dto.CartItemDto;
import com.shop.demoShop.entity.Cart;
import com.shop.demoShop.entity.CartItem;
import com.shop.demoShop.entity.Item;
import com.shop.demoShop.entity.Member;
import com.shop.demoShop.repository.CartItemRepository;
import com.shop.demoShop.repository.CartRepository;
import com.shop.demoShop.repository.ItemRepository;
import com.shop.demoShop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import com.shop.demoShop.dto.CartDetailDto;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import com.shop.demoShop.dto.CartOrderDto;
import com.shop.demoShop.dto.OrderDto;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

  private final ItemRepository itemRepository;
  private final MemberRepository memberRepository;
  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;
  private final OrderService orderService;

  public Long addCart(CartItemDto cartItemDto, String email) {

    Item item = itemRepository.findById(cartItemDto.getItemId())
        .orElseThrow(EntityNotFoundException::new); // 담을 상품 엔티티 조회
    Member member = memberRepository.findByEmail(email); // 로그인한 회원 엔티티 조회

    Cart cart = cartRepository.findByMemberId(member.getId()); // 로그인한 회원의 장바구니 엔티티 조회
    if (cart == null) { // 비어있으면 회원의 장바구니 엔티티 생성
      cart = Cart.createCart(member);
      cartRepository.save(cart);
    }

    CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId()); // 장바구니에 있는 상품인지 조회

    if (savedCartItem != null) { // 상품이 이미 있으면 수량을 추가
      savedCartItem.addCount(cartItemDto.getCount());
      return savedCartItem.getId();
    } else {
      CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount()); // 없으면 CartItem 엔티티 생성
      cartItemRepository.save(cartItem);
      return cartItem.getId();
    }
  }

  @Transactional(readOnly = true)
  public List<CartDetailDto> getCartList(String email) {

    List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

    Member member = memberRepository.findByEmail(email);
    Cart cart = cartRepository.findByMemberId(member.getId()); // 로그인한 회원의 장바구니 엔티티를 조회
    if (cart == null) { // 장바구니 엔티티가 없으면 빈 리스트 반환
      return cartDetailDtoList;
    }

    cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());
    return cartDetailDtoList;
  }

  @Transactional(readOnly = true)
  public boolean validateCartItem(Long cartItemId, String email) {
    Member curMember = memberRepository.findByEmail(email);
    CartItem cartItem = cartItemRepository.findById(cartItemId)
        .orElseThrow(EntityNotFoundException::new);
    Member savedMember = cartItem.getCart().getMember();

    if (!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) { // 현재 로그인한 회원과 장바구니 상품을 저장한 회원이 다를 경우falsel, 같으면 true* 반환
      return false;
    }

    return true;
  }

  public void updateCartItemCount(Long cartItemId, int count) { // 장바구니 상품의 수량을 업데이트
    CartItem cartItem = cartItemRepository.findById(cartItemId)
        .orElseThrow(EntityNotFoundException::new);

    cartItem.updateCount(count);
  }

  public void deleteCartItem(Long cartItemId) {
    CartItem cartItem = cartItemRepository.findById(cartItemId)
        .orElseThrow(EntityNotFoundException::new);
    cartItemRepository.delete(cartItem);
  }

  public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String email) {
    List<OrderDto> orderDtoList = new ArrayList<>();

    for (CartOrderDto cartOrderDto : cartOrderDtoList) { // orderDto 객체 생성
      CartItem cartItem = cartItemRepository
          .findById(cartOrderDto.getCartItemId())
          .orElseThrow(EntityNotFoundException::new);

      OrderDto orderDto = new OrderDto();
      orderDto.setItemId(cartItem.getItem().getId());
      orderDto.setCount(cartItem.getCount());
      orderDtoList.add(orderDto);
    }

    Long orderId = orderService.orders(orderDtoList, email); // 장바구니에 담은 상품을 주문하도록 주문 로직을 호출
    for (CartOrderDto cartOrderDto : cartOrderDtoList) { // 주문한 상품들은 장바구니에서 제거
      CartItem cartItem = cartItemRepository
          .findById(cartOrderDto.getCartItemId())
          .orElseThrow(EntityNotFoundException::new);
      cartItemRepository.delete(cartItem);
    }

    return orderId;
  }


}