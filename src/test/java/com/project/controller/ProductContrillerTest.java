package com.project.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:application-test.properties")
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("상품 등록 페이지 권한 테스트")
    @WithMockUser(username = "admin", roles = "ADMIN") //1. 이름 admin , ADMIN 유저가 로그인된 상태로 테스트 할 수 있도록하는 어노테이션
    public void itemFormTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/product/new")) //2. 상품 등록 페이지에 get 요청을 보냄
                .andDo(print()) //3. 요청과 응답 메시지를 확인 하는 콘솔 출력
                .andExpect(status().isOk()); // 4.  응답 상태 코드가 정상인지 확인
    }

    @Test
    @DisplayName("상품 등록 페이지 일반 회원 접근 테스트")
    @WithMockUser(username = "user", roles = "USER") // 1.  user USER 로 셋팅
    public void itemFormNotAdminTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/product/new"))
                .andDo(print())
                .andExpect(status().isForbidden()); // 2. 상품 등록 페이지에 집입 요청시 Forbidden 예외가 발생시 테스트가 성공으로 통과
    }
}