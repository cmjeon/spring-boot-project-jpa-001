package com.shop.demoShop.controller;

import com.shop.demoShop.base.BaseMockMvcTest;
import com.shop.demoShop.dto.CartDetailDto;
import com.shop.demoShop.dto.CartItemDto;
import com.shop.demoShop.service.CartService;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:application-test.properties")
@WithMockUser(username = "user'", roles = "USER")
class CartControllerTest extends BaseMockMvcTest {

    @MockBean
    CartService cartService;

    @BeforeAll
    public static void 모든_테스트_이전에_실행() {
        /**
         * static 으로 선언된 메소드여야 함
         */
        System.out.println("-- 모든 테스트 이전에 실행\n");
    }

    @BeforeEach
    public void 각_테스트_이전에_실행() {
        System.out.println("- 각 테스트 이전에 실행");
    }

    @Test
//    @WithMockUser(username = "user'", roles = "USER")
    public void 카트_아이템을_주문한다() throws Exception{
//        System.out.println("카트_아이템을_주문한다");
//        MultiValueMap<String, String> cartItem = new LinkedMultiValueMap<>();
//
//        cartItem.add("itemId", "1L");
//        cartItem.add("count", "chip");
        // given
        CartItemDto ci = new CartItemDto();
        ci.setItemId(1L);
        ci.setCount(1);
        String content = objectMapper.writeValueAsString(ci);

        given(cartService.addCart(any(CartItemDto.class), any(String.class))).willReturn(1L);

        mockMvc.perform(post("/cart")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
//            .andExpect(content().string("칩의 블로그입니다. chip"))
//            .andDo(print());
    }

    @Test
    public void 카트_아이템을_갱신한다() {
        System.out.println("카트_아이템을_갱신한다");
    }

    @Test
//    @WithMockUser(username = "user'", roles = "USER")
    public void 카드_아이템을_조회한다() throws Exception {
        // given
        List<CartDetailDto> cartDetailList = Arrays.asList(
            new CartDetailDto(1L, "아이템1", 1000, 1, ""),
            new CartDetailDto(2L, "아이템2", 2000, 2, "")
        );
        given(cartService.getCartList(any(String.class))).willReturn(cartDetailList);

        mockMvc.perform(
            get("/cart")
            )
            .andExpect(status().isOk());
    }

    @AfterEach
    public void 각_테스트_이후에_실행() {
        System.out.println("- 각 테스트 이후에 실행\n");
    }

    @AfterAll
    public static void 모든_테스트_이후에_실행() {
        /**
         * static 으로 선언된 메소드여야 함
         */
        System.out.println("-- 모든 테스트 이후에 실행\n");
    }
}