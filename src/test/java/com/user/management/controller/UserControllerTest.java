package com.user.management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.management.dto.UserCreateRequest;
import com.user.management.dto.UserDataResponse;
import com.user.management.dto.UserLoginRequest;
import com.user.management.entity.Role;
import com.user.management.entity.Status;
import com.user.management.entity.User;
import com.user.management.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserService userService;
    @Autowired
    ObjectMapper objectMapper;
    HttpHeaders httpHeaders = new HttpHeaders();
    UserCreateRequest userCreateRequest;
    UserLoginRequest userLoginRequest;
    User user;
    UserDataResponse userDataResponse1;
    UserDataResponse userDataResponse2;

    @BeforeEach
    void init() {
        String mockUserId = "mockUserId";
        this.httpHeaders.add("X-USER-ID", mockUserId);

        userCreateRequest = new UserCreateRequest(
                "userCreateRequestId",
                "testName",
                "testPassword",
                "testEmail",
                "testBirth"
        );
        userLoginRequest = new UserLoginRequest(
                "userLoginRequestId",
                "testPassword"
        );
        user = User.builder()
                .id("userId").build();
        userDataResponse1 = new UserDataResponse(
                "userDataResponse1Id",
                "testName",
                "testEmail",
                "testBirth",
                "testRole",
                "teststauts"
        );
        userDataResponse2 = new UserDataResponse(
                "userDataResponse2Id",
                "testName",
                "testEmail",
                "testBirth",
                "testRole",
                "teststauts"
        );
    }

    @Test
    @Order(1)
    @DisplayName("유저 전체 조회")
    void findAllUsers() throws Exception {
        List<UserDataResponse> testList = new ArrayList<>();
        testList.add(userDataResponse1);
        testList.add(userDataResponse2);
        given(userService.getAllUsers(any(String.class))).willReturn(testList);

        mockMvc.perform(get("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.[1].id").value("userDataResponse2Id"));
    }

    @Test
    @Order(2)
    @DisplayName("유저 단건 조회")
    void findUser() throws Exception {
        given(userService.getUserById(any(String.class))).willReturn(userDataResponse1);

        mockMvc.perform(get("/api/user/myPage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value("userDataResponse1Id"));
    }

    @Test
    @Order(3)
    @DisplayName("유저 등록")
    void createUser() throws Exception {
        Mockito.doNothing().when(userService).createUser(any(UserCreateRequest.class));

        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(4)
    @DisplayName("로그인")
    void loginUser() throws Exception {
        given(userService.getUserLogin(any(UserLoginRequest.class))).willReturn(userDataResponse1);

        mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginRequest))
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value("userDataResponse1Id"));
    }

    @Test
    @Order(5)
    @DisplayName("유저 수정")
    void updateUser() throws Exception {
        Mockito.doNothing().when(userService).updateUser(any(UserCreateRequest.class),any(String.class));

        mockMvc.perform(put("/api/user/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(httpHeaders)
                    .content(objectMapper.writeValueAsString(userCreateRequest)))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(6)
    @DisplayName("유저 삭제")
    void deleteUser() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(any(String.class));

        mockMvc.perform(delete("/api/user/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(httpHeaders))
                .andExpect(status().isNoContent());
    }

}
