package com.shop.demoShop.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.demoShop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

public abstract class BaseMockMvcTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

}
