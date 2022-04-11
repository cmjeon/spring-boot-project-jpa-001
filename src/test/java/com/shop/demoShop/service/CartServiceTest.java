package com.shop.demoShop.service;

import com.shop.demoShop.constant.ItemSellStatus;
import com.shop.demoShop.dto.CartItemDto;
import com.shop.demoShop.entity.CartItem;
import com.shop.demoShop.entity.Item;
import com.shop.demoShop.entity.Member;
import com.shop.demoShop.repository.CartItemRepository;
import com.shop.demoShop.repository.ItemRepository;
import com.shop.demoShop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class CartServiceTest {

  @Autowired
  ItemRepository itemRepository;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  CartService cartService;

  @Autowired
  CartItemRepository cartItemRepository;

  public Item saveItem(){ // 테스트용 상품정보
    Item item = new Item();
    item.setItemNm("테스트 상품");
    item.setPrice(10000);
    item.setItemDetail("테스트 상품 상세 설명");
    item.setItemSellStatus(ItemSellStatus.SELL);
    item.setStockNumber(100);
    return itemRepository.save(item);
  }

  public Member saveMember(){ // 테스트용 회원정보
    Member member = new Member();
    member.setEmail("test@test.com");
    return memberRepository.save(member);
  }

  @Test
  @DisplayName("장바구니 담기 테스트")
  public void addCart(){
    Item item = saveItem();
    Member member = saveMember();

    CartItemDto cartItemDto = new CartItemDto();
    cartItemDto.setCount(5);
    cartItemDto.setItemId(item.getId());

    Long cartItemId = cartService.addCart(cartItemDto, member.getEmail());  // 상품을 장바구니에 담는 로직 호출
    CartItem cartItem = cartItemRepository.findById(cartItemId)
        .orElseThrow(EntityNotFoundException::new); // 장바구니 상품 정보 조회

    assertEquals(item.getId(), cartItem.getItem().getId()); // 상품 아이디와 장바구니에 저장된 상품 아이디가 같으면 통과
    assertEquals(cartItemDto.getCount(), cartItem.getCount()); // 장바구니의 상품 수량과 장바구니에 저장된 상품 수량이 같으면 통과
  }

}