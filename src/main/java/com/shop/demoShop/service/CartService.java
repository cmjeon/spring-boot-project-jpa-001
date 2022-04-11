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

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

  private final ItemRepository itemRepository;
  private final MemberRepository memberRepository;
  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;
  private final OrderService orderService;

  public Long addCart(CartItemDto cartItemDto, String email){

    Item item = itemRepository.findById(cartItemDto.getItemId())
        .orElseThrow(EntityNotFoundException::new); // 담을 상품 엔티티 조회
    Member member = memberRepository.findByEmail(email); // 로그인한 회원 엔티티 조회

    Cart cart = cartRepository.findByMemberId(member.getId()); // 로그인한 회원의 장바구니 엔티티 조회
    if(cart == null){ // 비어있으면 회원의 장바구니 엔티티 생성
      cart = Cart.createCart(member);
      cartRepository.save(cart);
    }

    CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId()); // 장바구니에 있는 상품인지 조회

    if(savedCartItem != null){ // 상품이 이미 있으면 수량을 추가
      savedCartItem.addCount(cartItemDto.getCount());
      return savedCartItem.getId();
    } else {
      CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount()); // 없으면 CartItem 엔티티 생성
      cartItemRepository.save(cartItem);
      return cartItem.getId();
    }
  }

}