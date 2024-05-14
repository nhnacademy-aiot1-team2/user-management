package com.user.management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.management.dto.DeleteUserRequest;
import com.user.management.dto.PermitUserRequest;
import com.user.management.dto.UserDataResponse;
import com.user.management.exception.RoleNotFoundException;
import com.user.management.exception.StatusNotFoundException;
import com.user.management.exception.UserNotFoundException;
import com.user.management.page.RestPage;
import com.user.management.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    void findAllUsers() throws Exception {
        int page = 0;
        int size = 10;
        String userId = "test user";
        String name = "test name";
        String email = "test@gmail.com";
        String roleName = "test role";
        String statusName = "test status";
        String password = "test password";
        UserDataResponse userDataResponse = UserDataResponse.builder()
                .id(userId)
                .name(name)
                .email(email)
                .roleName(roleName)
                .statusName(statusName)
                .password(password)
                .build();

        given(userService.getAllUsers(any()))
                .willReturn(new RestPage<>(new PageImpl<>(List.of(userDataResponse))));

        mockMvc.perform(get("/api/user/admin/userList")
                        .param("page", Integer.toString(page))
                        .param("size", Integer.toString(size)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", equalTo(userId)))
                .andExpect(jsonPath("$.content[0].name", equalTo(name)))
                .andExpect(jsonPath("$.content[0].email", equalTo(email)))
                .andExpect(jsonPath("$.content[0].roleName", equalTo(roleName)))
                .andExpect(jsonPath("$.content[0].statusName", equalTo(statusName)))
                .andExpect(jsonPath("$.content[0].password", equalTo(password)));
    }

    @Test
    void findAllUsersUserNotFoundException() throws Exception {
        int page = 0;
        int size = 10;

        given(userService.getAllUsers(any()))
                .willThrow(new UserNotFoundException("test"));

        mockMvc.perform(get("/api/user/admin/userList")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("test는 존재하지 않는 userId 입니다.")));
    }

    @Test
    void findSortedUserByStatusId() throws Exception {
        String userId = "test user";
        int page = 0;
        int size = 10;
        long statusId = 1L;
        String name = "test name";
        String email = "test@gmail.com";
        String roleName = "test role";
        String statusName = "test status";
        String password = "test password";
        UserDataResponse userDataResponse = UserDataResponse.builder()
                .id(userId)
                .name(name)
                .email(email)
                .roleName(roleName)
                .statusName(statusName)
                .password(password)
                .build();

        given(userService.getFilteredUsersByStatus(anyLong(), any()))
                .willReturn(new RestPage<>(new PageImpl<>(List.of(userDataResponse))));

        mockMvc.perform(get("/api/user/admin/userList/sort/status/" + statusId)
                        .header("X-USER-ID", userId)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", equalTo(userId)))
                .andExpect(jsonPath("$.content[0].name", equalTo(name)))
                .andExpect(jsonPath("$.content[0].email", equalTo(email)))
                .andExpect(jsonPath("$.content[0].roleName", equalTo(roleName)))
                .andExpect(jsonPath("$.content[0].statusName", equalTo(statusName)))
                .andExpect(jsonPath("$.content[0].password", equalTo(password)));
    }

    @Test
    void findSortedUserByStatusIdStatusNotFoundException() throws Exception {
        String userId = "test user";
        int page = 0;
        int size = 10;
        long statusId = 1L;

        given(userService.getFilteredUsersByStatus(anyLong(), any()))
                .willThrow(new StatusNotFoundException("not found status"));

        mockMvc.perform(get("/api/user/admin/userList/sort/status/" + statusId)
                        .header("X-USER-ID", userId)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("not found status")));
    }

    @Test
    void findSortedUserByRoleId() throws Exception {
        String userId = "test user";
        int page = 0;
        int size = 10;
        long roleId = 1L;

        String name = "test name";
        String email = "test@gmail.com";
        String roleName = "test role";
        String statusName = "test status";
        String password = "test password";
        UserDataResponse userDataResponse = UserDataResponse.builder()
                .id(userId)
                .name(name)
                .email(email)
                .roleName(roleName)
                .statusName(statusName)
                .password(password)
                .build();

        given(userService.getFilteredUsersByRole(anyLong(), any()))
                .willReturn(new RestPage<>(new PageImpl<>(List.of(userDataResponse))));

        mockMvc.perform(get("/api/user/admin/userList/sort/role/" + roleId)
                        .header("X-USER-ID", userId)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", equalTo(userId)))
                .andExpect(jsonPath("$.content[0].name", equalTo(name)))
                .andExpect(jsonPath("$.content[0].email", equalTo(email)))
                .andExpect(jsonPath("$.content[0].roleName", equalTo(roleName)))
                .andExpect(jsonPath("$.content[0].statusName", equalTo(statusName)))
                .andExpect(jsonPath("$.content[0].password", equalTo(password)));
    }

    @Test
    void findSortedUserByRoleIdStatusNotFoundException() throws Exception {
        String userId = "test user";
        int page = 0;
        int size = 10;
        long statusId = 1L;

        given(userService.getFilteredUsersByRole(anyLong(), any()))
                .willThrow(new RoleNotFoundException("not found role"));

        mockMvc.perform(get("/api/user/admin/userList/sort/role/" + statusId)
                        .header("X-USER-ID", userId)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("not found role")));
    }

    @Test
    void promoteUserToAdmin() throws Exception {
        String userId = "test user";
        String name = "test name";
        String email = "test email";
        String roleName = "test role";
        String statusName = "test status";
        String password = "test password";
        PermitUserRequest permitUserRequest = new PermitUserRequest(userId);
        ObjectMapper objectMapper = new ObjectMapper();
        UserDataResponse userDataResponse = UserDataResponse.builder()
                .id(userId)
                .name(name)
                .email(email)
                .roleName(roleName)
                .statusName(statusName)
                .password(password)
                .build();

        given(userService.permitUser(any()))
                .willReturn(userDataResponse);

        mockMvc.perform(post("/api/user/admin/promotion")
                        .content(objectMapper.writeValueAsString(List.of(permitUserRequest)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void promoteUserToAdminUserNotFoundException() throws Exception {
        String userId = "test user";
        PermitUserRequest promotionUserRequest = new PermitUserRequest(userId);
        ObjectMapper objectMapper = new ObjectMapper();

        given(userService.promoteUser(any()))
                .willThrow(new UserNotFoundException(userId));

        mockMvc.perform(post("/api/user/admin/promotion")
                        .content(objectMapper.writeValueAsString(List.of(promotionUserRequest)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("test user는 존재하지 않는 userId 입니다.")));
    }

    @Test
    void permitUser() throws Exception {
        String userId = "test user";
        String name = "test name";
        String email = "test email";
        String roleName = "test role";
        String statusName = "test status";
        String password = "test password";
        PermitUserRequest permitUserRequest = new PermitUserRequest(userId);
        ObjectMapper objectMapper = new ObjectMapper();
        UserDataResponse userDataResponse = UserDataResponse.builder()
                .id(userId)
                .name(name)
                .email(email)
                .roleName(roleName)
                .statusName(statusName)
                .password(password)
                .build();

        given(userService.permitUser(any()))
                .willReturn(userDataResponse);

        mockMvc.perform(post("/api/user/admin/permit")
                        .content(objectMapper.writeValueAsString(List.of(permitUserRequest)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void permitUserUserNotFoundException() throws Exception {
        String userId = "test user";
        PermitUserRequest permitUserRequest = new PermitUserRequest(userId);
        ObjectMapper objectMapper = new ObjectMapper();

        given(userService.permitUser(any()))
                .willThrow(new UserNotFoundException(userId));

        mockMvc.perform(post("/api/user/admin/permit")
                        .content(objectMapper.writeValueAsString(List.of(permitUserRequest)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("test user는 존재하지 않는 userId 입니다.")));
    }

    @Test
    void rejectDeleteUser() throws Exception {
        String userId = "test user";
        String name = "test name";
        String email = "test email";
        String roleName = "test role";
        String statusName = "test status";
        String password = "test password";
        DeleteUserRequest deleteUserRequest = new DeleteUserRequest(userId);
        ObjectMapper objectMapper = new ObjectMapper();
        UserDataResponse userDataResponse = UserDataResponse.builder()
                .id(userId)
                .name(name)
                .email(email)
                .roleName(roleName)
                .statusName(statusName)
                .password(password)
                .build();

        given(userService.permitUser(any()))
                .willReturn(userDataResponse);

        mockMvc.perform(post("/api/user/admin/reject/delete")
                        .content(objectMapper.writeValueAsString(List.of(deleteUserRequest)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void rejectDeleteUserUserNotFoundException() throws Exception {
        String userId = "test user";
        DeleteUserRequest deleteUserRequest = new DeleteUserRequest(userId);
        ObjectMapper objectMapper = new ObjectMapper();

        given(userService.permitUser(any()))
                .willThrow(new UserNotFoundException(userId));

        mockMvc.perform(post("/api/user/admin/reject/delete")
                        .content(objectMapper.writeValueAsString(List.of(deleteUserRequest)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("test user는 존재하지 않는 userId 입니다.")));
    }

    @Test
    void deleteUser() throws Exception {
        String userId = "test user";
        DeleteUserRequest deleteUserRequest = new DeleteUserRequest(userId);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(delete("/api/user/admin/delete")
                        .content(objectMapper.writeValueAsString(List.of(objectMapper.writeValueAsString(deleteUserRequest))))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}