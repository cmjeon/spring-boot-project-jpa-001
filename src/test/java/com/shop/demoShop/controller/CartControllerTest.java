package com.shop.demoShop.controller;

import org.junit.jupiter.api.*;

class CartControllerTest {

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
    @DisplayName("카트 아이템을 주문한다")
    public void 카트_아이템을_주문한다() {
        System.out.println("카트_아이템을_주문한다");
    }

    @Test
    @DisplayName("카트 아이템을 갱신한다")
    public void 카트_아이템을_갱신한다() {
        System.out.println("카트_아이템을_갱신한다");
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