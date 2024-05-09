package com.user.management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.management.dto.*;
import com.user.management.exception.*;
import com.user.management.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    void findUser() throws Exception {
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

        given(userService.getUserById(anyString()))
                .willReturn(userDataResponse);

        mockMvc.perform(get("/api/user/myPage")
                        .header("X-USER-ID", userId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(userId)))
                .andExpect(jsonPath("$.name", equalTo(name)))
                .andExpect(jsonPath("$.email", equalTo(email)))
                .andExpect(jsonPath("$.roleName", equalTo(roleName)))
                .andExpect(jsonPath("$.statusName", equalTo(statusName)))
                .andExpect(jsonPath("$.password", equalTo(password)));
    }

    @Test
    void findUserUserNotFoundException() throws Exception {
        String userId = "test user";

        given(userService.getUserById(anyString()))
                .willThrow(new UserNotFoundException(userId));

        mockMvc.perform(get("/api/user/myPage")
                        .header("X-USER-ID", userId))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("test user는 존재하지 않는 userId 입니다.")));
    }

    @Test
    void createUser() throws Exception {
        String userId = "test user";
        String name = "test name";
        String password = "test password";
        String email = "test email";
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        ObjectMapper objectMapper = new ObjectMapper();

        userCreateRequest.setId(userId);
        userCreateRequest.setName(name);
        userCreateRequest.setPassword(password);
        userCreateRequest.setEmail(email);

        mockMvc.perform(post("/api/user/register")
                        .content(objectMapper.writeValueAsString(userCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(userService, times(1)).createUser(any());
    }

    @Test
    void createUserUserAlreadyExistException() throws Exception {
        String userId = "test user";
        String name = "test name";
        String password = "test password";
        String email = "test email";
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        ObjectMapper objectMapper = new ObjectMapper();

        userCreateRequest.setId(userId);
        userCreateRequest.setName(name);
        userCreateRequest.setPassword(password);
        userCreateRequest.setEmail(email);

        given(userService.createUser(any()))
                .willThrow(new UserAlreadyExistException(userId));

        mockMvc.perform(post("/api/user/register")
                        .content(objectMapper.writeValueAsString(userCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("test user는 이미 존재하는 유저 아이디 입니다.")));
    }

    @Test
    void createUserAlreadyExistEmailException() throws Exception {
        String userId = "test user";
        String name = "test name";
        String password = "test password";
        String email = "test email";
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        ObjectMapper objectMapper = new ObjectMapper();

        userCreateRequest.setId(userId);
        userCreateRequest.setName(name);
        userCreateRequest.setPassword(password);
        userCreateRequest.setEmail(email);

        given(userService.createUser(any()))
                .willThrow(new AlreadyExistEmailException(email));

        mockMvc.perform(post("/api/user/register")
                        .content(objectMapper.writeValueAsString(userCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("test email은 이미 존재하는 이메일입니다.")));
    }

    @Test
    void loginUser() throws Exception {
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
        UserLoginRequest userLoginRequest = new UserLoginRequest(userId, password);
        ObjectMapper objectMapper = new ObjectMapper();

        given(userService.getUserLogin(any()))
                .willReturn(userDataResponse);

        mockMvc.perform(post("/api/user/login")
                        .content(objectMapper.writeValueAsString(userLoginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(userId)))
                .andExpect(jsonPath("$.name", equalTo(name)))
                .andExpect(jsonPath("$.email", equalTo(email)))
                .andExpect(jsonPath("$.roleName", equalTo(roleName)))
                .andExpect(jsonPath("$.statusName", equalTo(statusName)))
                .andExpect(jsonPath("$.password", equalTo(password)));
    }

    @Test
    void loginUserUserNotFoundException() throws Exception {
        String userId = "test user";
        String password = "test password";
        UserLoginRequest userLoginRequest = new UserLoginRequest(userId, password);
        ObjectMapper objectMapper = new ObjectMapper();

        given(userService.getUserLogin(any()))
                .willThrow(new UserNotFoundException(userId));

        mockMvc.perform(post("/api/user/login")
                        .content(objectMapper.writeValueAsString(userLoginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("test user는 존재하지 않는 userId 입니다.")));
    }

    @Test
    void loginUserAdminMustUpdatePasswordException() throws Exception {
        String userId = "test user";
        String password = "test password";
        UserLoginRequest userLoginRequest = new UserLoginRequest(userId, password);
        ObjectMapper objectMapper = new ObjectMapper();

        given(userService.getUserLogin(any()))
                .willThrow(new AdminMustUpdatePasswordException());

        mockMvc.perform(post("/api/user/login")
                        .content(objectMapper.writeValueAsString(userLoginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("어드민은 첫 로그인 시, 반드시 초기 비밀번호를 변경해야 합니다!!!")));
    }

    @Test
    void loginUserInvalidPasswordException() throws Exception {
        String userId = "test user";
        String password = "test password";
        UserLoginRequest userLoginRequest = new UserLoginRequest(userId, password);
        ObjectMapper objectMapper = new ObjectMapper();

        given(userService.getUserLogin(any()))
                .willThrow(new InvalidPasswordException());

        mockMvc.perform(post("/api/user/login")
                        .content(objectMapper.writeValueAsString(userLoginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("패스워드가 올바르지 않습니다.")));
    }

    @Test
    void updateUser() throws Exception {
        String userId = "test user";
        String name = "test name";
        String password = "password";
        String email = "test email";
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        ObjectMapper objectMapper = new ObjectMapper();

        userUpdateRequest.setName(name);
        userUpdateRequest.setPassword(password);
        userUpdateRequest.setEmail(email);

        mockMvc.perform(put("/api/user/update")
                        .header("X-USER-ID", userId)
                        .content(objectMapper.writeValueAsString(userUpdateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(userService, times(1)).updateUser(any(), anyString());
    }

    @Test
    void updateUserUserHeaderNotFoundException() throws Exception {
        String userId = "test user";
        String name = "test name";
        String password = "password";
        String email = "test email";
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        ObjectMapper objectMapper = new ObjectMapper();

        userUpdateRequest.setName(name);
        userUpdateRequest.setPassword(password);
        userUpdateRequest.setEmail(email);

        given(userService.updateUser(any(), anyString()))
                .willThrow(new UserHeaderNotFoundException());

        mockMvc.perform(put("/api/user/update")
                        .header("X-USER-ID", userId)
                        .content(objectMapper.writeValueAsString(userUpdateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("X-USER-ID header 값이 필요한 요청입니다.")));
    }

    @Test
    void updateUserAlreadyExistEmailException() throws Exception {
        String userId = "test user";
        String name = "test name";
        String password = "password";
        String email = "test email";
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        ObjectMapper objectMapper = new ObjectMapper();

        userUpdateRequest.setName(name);
        userUpdateRequest.setPassword(password);
        userUpdateRequest.setEmail(email);

        given(userService.updateUser(any(), anyString()))
                .willThrow(new AlreadyExistEmailException(email));

        mockMvc.perform(put("/api/user/update")
                        .header("X-USER-ID", userId)
                        .content(objectMapper.writeValueAsString(userUpdateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("test email은 이미 존재하는 이메일입니다.")));
    }

    @Test
    void deactivateUser() throws Exception {
        String userId = "test user";

        mockMvc.perform(post("/api/user/deactivate")
                        .header("X-USER-ID", userId))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deactivateUser(anyString());
    }

    @Test
    void deactivateUserUserHeaderNotFoundException() throws Exception {
        String userId = "test user";

        given(userService.deactivateUser(any()))
                .willThrow(new UserHeaderNotFoundException());

        mockMvc.perform(post("/api/user/deactivate")
                        .header("X-USER-ID", userId))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("X-USER-ID header 값이 필요한 요청입니다.")));
    }

    @Test
    void getRoleId() throws Exception {
        String userId = "test user";
        Long roleId = 1L;
        String roleName = "test role";
        RoleResponse roleResponse = new RoleResponse();

        roleResponse.setRoleId(roleId);
        roleResponse.setRoleName(roleName);

        given(userService.getRoleByUserId(anyString()))
                .willReturn(roleResponse);

        mockMvc.perform(get("/api/user/role")
                        .header("X-USER-ID", userId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleId", equalTo(roleId.intValue())))
                .andExpect(jsonPath("$.roleName", equalTo(roleName)));
    }
}