package com.user.management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.management.dto.UserCreateRequest;
import com.user.management.entity.Role;
import com.user.management.entity.Status;
import com.user.management.entity.User;
import com.user.management.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    UserCreateRequest userCreateRequest = new UserCreateRequest();
    HttpHeaders httpHeaders = new HttpHeaders();
    User testUser1;
    User testUser2;

    @BeforeEach
    void init() {
        this.userCreateRequest.setId("testId");
        this.userCreateRequest.setName("testName");
        this.userCreateRequest.setPassword("testPassword");
        this.userCreateRequest.setEmail("testEmail");
        this.userCreateRequest.setBirth("testBirth");

        String mockUserId = "mockUserId";
        this.httpHeaders.add("X-USER-ID", mockUserId);

        testUser1 = new User("testId",
                "testName",
                "testPassword",
                "testEmail",
                "testBirth",
                new Role(1L, "testName"),
                new Status(1L, "testName"),
                null,
                null);
        testUser2 = new User("testId2",
                "testName2",
                "testPassword2",
                "testEmail2",
                "testBirth2",
                new Role(1L, "testName"),
                new Status(1L, "testName"),
                null,
                null);
    }

    @Test
    @DisplayName("유저 전체 조회")
    void findAllUsers() throws Exception {
        List<User> testList = new ArrayList<>();
        testList.add(testUser1);
        testList.add(testUser2);
        given(userService.getAllUsers(any(String.class))).willReturn(testList);

        mockMvc.perform(get("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.[0].id").value("testId"));
    }

    @Test
    @DisplayName("유저 단건 조회")
    void findUser() throws Exception {
        given(userService.getUserById(any(String.class))).willReturn(testUser1);

        mockMvc.perform(get("/api/user/myPage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value("testId"));
    }

    @Test
    @DisplayName("유저 등록")
    void createUser() throws Exception {
        Mockito.doNothing().when(userService).createUser(any(UserCreateRequest.class));

        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("유저 수정")
    void updateUser() throws Exception {
        Mockito.doNothing().when(userService).updateUser(any(UserCreateRequest.class),any(String.class));

        mockMvc.perform(put("/api/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(httpHeaders)
                    .content(objectMapper.writeValueAsString(userCreateRequest)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("유저 삭제")
    void deleteUser() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(any(String.class));

        mockMvc.perform(delete("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(httpHeaders))
                .andExpect(status().isNoContent());
    }

}
