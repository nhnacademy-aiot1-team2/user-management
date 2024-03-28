package com.user.management.service;

import com.user.management.dto.UserCreateRequest;
import com.user.management.dto.UserDataResponse;
import com.user.management.dto.UserLoginRequest;
import com.user.management.entity.Role;
import com.user.management.entity.Status;
import com.user.management.entity.User;
import com.user.management.repository.RoleRepository;
import com.user.management.repository.StatusRepository;
import com.user.management.repository.UserRepository;
import com.user.management.service.impl.UserServiceImpl;
import com.user.management.util.CryptoUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    RoleRepository roleRepository;
    @Mock
    StatusRepository statusRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserServiceImpl userService;

    @Test
    @Order(1)
    void getAllUsers() {
        UserDataResponse testU1 = new UserDataResponse();
        UserDataResponse testU2 = new UserDataResponse();

        List<UserDataResponse> expect = new ArrayList<>();
        expect.add(testU1);
        expect.add(testU2);

        Mockito.when(userRepository.existsById(any(String.class))).thenReturn(true);
        Mockito.when(userRepository.getRoleByUserId(any(String.class))).thenReturn(new Role(1L, "TEST_ROLE"));
        Mockito.when(userRepository.getAllUserData()).thenReturn(expect);

        Assertions.assertEquals(expect, userService.getAllUsers("testId"));
    }

    @Test
    @Order(2)
    void getUserById() {
        String userId = "testUserId";
        UserDataResponse data = new UserDataResponse();
        data.setId(userId);

        Mockito.when(userRepository.getUserById(userId)).thenReturn(Optional.of(data));

        UserDataResponse result = userService.getUserById(userId);

        Assertions.assertEquals(userId, result.getId());
    }

    @Test
    @Order(3)
    void getUserLogin() throws Exception {
        String sha256password = CryptoUtil.sha256("testPassword", "1");
        UserLoginRequest userLoginRequest = new UserLoginRequest("testId", "testPassword");
        User user = User.builder()
                .id("testId")
                .name("")
                .email("")
                .birth("")
                .role(new Role(1L, "ROLE_TEST"))
                .status(new Status(1L, "STATUS_TEST"))
                .password(sha256password)
                .salt("1")
                .latestLoginAt(LocalDateTime.now())
                .build();
        UserDataResponse userDataResponse = new UserDataResponse("testId",
                "",
                "",
                "",
                "",
                "");

        Mockito.when(userRepository.findById(userLoginRequest.getId())).thenReturn(Optional.ofNullable(user));

        Assertions.assertInstanceOf(UserDataResponse.class, userService.getUserLogin(userLoginRequest));
        Assertions.assertEquals(userDataResponse.getId(), userService.getUserLogin(userLoginRequest).getId());
    }

    @Test
    @Order(4)
    void createUser() {

        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setId("testId");

        Mockito.when(userRepository.existsById(any(String.class))).thenReturn(false);
        Mockito.when(roleRepository.getUserRole()).thenReturn(new Role(1L, "ROLE_TEST"));
        Mockito.when(statusRepository.getActiveStatus()).thenReturn(new Status(1L, "STATUS_TEST"));

        userService.createUser(userCreateRequest);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userArgumentCaptor.capture());
        Assertions.assertEquals("testId", userArgumentCaptor.getValue().getId());
    }

    @Test
    @Order(5)
    void updateUser() {
        UserCreateRequest userCreateRequest = new UserCreateRequest("changeTestId",
                "testName",
                "testPassword",
                "testEmail",
                "testBirth");
        String userId = "originalId";
        User originalUser = User.builder()
                        .name("originalId").build();

        Mockito.when(userRepository.existsById(any(String.class))).thenReturn(false);
        Mockito.when(userRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(originalUser));

        userService.updateUser(userCreateRequest, userId);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userArgumentCaptor.capture());
        Assertions.assertEquals("changeTestId", userArgumentCaptor.getValue().getId());
    }

    @Test
    @Order(6)
    void deleteUser() {
        User user = User.builder().id("testId").build();

        Mockito.when(userRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(user));
        Mockito.when(statusRepository.getDeactivatedStatus()).thenReturn(new Status(1L, "STATUS_TEST"));

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        userService.deleteUser("testId");

        verify(userRepository).save(userArgumentCaptor.capture());
    }

}
