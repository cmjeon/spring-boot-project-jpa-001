package com.shop.demoShop.service;

import com.shop.demoShop.dto.CartDetailDto;
import com.shop.demoShop.entity.Cart;
import com.shop.demoShop.entity.Member;
import com.shop.demoShop.repository.CartItemRepository;
import com.shop.demoShop.repository.CartRepository;
import com.shop.demoShop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;

@ExtendWith(MockitoExtension.class)
public class MockCartServiceTest {

    @InjectMocks
    CartService cartService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    CartRepository cartRepository;

    @Mock
    CartItemRepository cartItemRepository;

    @Test
    void 카트목록을_가져온다() {
        // given
        String email = "aaa@aaa.com";

        Member member = new Member();
        member.setId(1L);
        member.setName("안승태");
        willReturn(member).given(memberRepository).findByEmail(any(String.class));

        Cart cart = new Cart();
        cart.setId(10L);
        willReturn(cart).given(cartRepository).findByMemberId(member.getId());

        List<CartDetailDto> cartDetailDtoList = List.of(new CartDetailDto(
            1L,"",0,0, ""
            ),
            new CartDetailDto(
                2L,"",0,0, ""
            ));
        willReturn(cartDetailDtoList).given(cartItemRepository).findCartDetailDtoList(cart.getId());

        // when
        List<CartDetailDto> actualCartDetailDtoList = cartService.getCartList(email);

        // then
        assertThat(actualCartDetailDtoList).isNotNull();
        assertThat(actualCartDetailDtoList.get(0).getCartItemId()).isEqualTo(1L);
    }

}
