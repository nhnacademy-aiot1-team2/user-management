package com.user.management.service;

import com.user.management.dto.UserCreateRequest;
import com.user.management.dto.UserDataResponse;
import com.user.management.dto.UserLoginRequest;
import com.user.management.entity.Role;
import com.user.management.entity.Status;
import com.user.management.entity.User;
import com.user.management.exception.*;
import com.user.management.repository.RoleRepository;
import com.user.management.repository.StatusRepository;
import com.user.management.repository.UserRepository;
import com.user.management.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    RoleRepository roleRepository;
    @Mock
    StatusRepository statusRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    UserServiceImpl userService;

    @Test
    @Order(1)
    @DisplayName("유저 전체 조회")
    void getAllUsers() {
        UserDataResponse testUser1 = new UserDataResponse();
        UserDataResponse testUser2 = new UserDataResponse();

        List<UserDataResponse> testList = new ArrayList<>();
        testList.add(testUser1);
        testList.add(testUser2);
        Page<UserDataResponse> expect = new PageImpl<>(testList);

        Pageable pageable = PageRequest.of(0, 5);

        given(userRepository.existsById(any(String.class))).willReturn(true);
        given(userRepository.getRoleByUserId(any(String.class))).willReturn(new Role(1L, "TEST_ROLE"));
        given(userRepository.getAllUserData(any(Pageable.class))).willReturn(expect);

        Assertions.assertEquals(2, userService.getAllUsers("testId", pageable).getTotalElements());
    }

    @Test
    @Order(2)
    @DisplayName("유저 전체 조회 : DB에 id와 일치하는 유저 정보가 없을 때")
    void getAllUsers_UserNotFoundException(){
        Pageable pageable = PageRequest.of(0, 5);

        given(userRepository.existsById(any(String.class))).willReturn(false);

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getAllUsers("testId", pageable));
    }

    @Test
    @Order(3)
    @DisplayName("유저 전체 조회 : id와 일치하는 유저 정보가 1L(관리자)가 아닐 때")
    void getAllUsers_OnlyAdminCanAccessUserDataException(){
        Pageable pageable = PageRequest.of(0, 5);

        given(userRepository.existsById(any(String.class))).willReturn(true);
        given(userRepository.getRoleByUserId(any(String.class))).willReturn(new Role(2L, "TEST_ROLE"));

        Assertions.assertThrows(OnlyAdminCanAccessUserDataException.class, () -> userService.getAllUsers("testId", pageable));
    }

    @Test
    @Order(4)
    @DisplayName("유저 단건 조회")
    void getUserById() {
        String userId = "testUserId";
        UserDataResponse data = new UserDataResponse();
        data.setId(userId);

        given(userRepository.getUserById(userId)).willReturn(Optional.of(data));

        UserDataResponse result = userService.getUserById(userId);

        Assertions.assertEquals(userId, result.getId());
    }

    @Test
    @Order(5)
    @DisplayName("유저 단건 조회 : DB에 id와 일치하는 유저 정보가 없을 때")
    void getUserById_UserNotFoundException(){
        given(userRepository.getUserById(anyString())).willReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUserById(anyString()));
    }

    @Test
    @Order(6)
    @DisplayName("로그인")
    void getUserLogin(){
        UserLoginRequest userLoginRequest = new UserLoginRequest("testId", "testPassword");
        User user = User.builder()
                .id("testId")
                .name("")
                .email("")
                .birth("")
                .role(new Role(1L, "ROLE_TEST"))
                .status(new Status(1L, "STATUS_TEST"))
                .password("testPassword")
                .latestLoginAt(LocalDateTime.now())
                .build();
        UserDataResponse userDataResponse = new UserDataResponse("testId",
                "",
                "",
                "",
                "",
                "");

        given(userRepository.findById(userLoginRequest.getId())).willReturn(Optional.ofNullable(user));
        given(passwordEncoder.matches(any(), any())).willReturn(true);

        Assertions.assertInstanceOf(UserDataResponse.class, userService.getUserLogin(userLoginRequest));
        Assertions.assertEquals(userDataResponse.getId(), userService.getUserLogin(userLoginRequest).getId());
    }

    @Test
    @Order(7)
    @DisplayName("로그인 : DB 정보와 Request 에서 받은 유저 정보 비교 후 존재 여부 판명")
    void getUserLogin_UserNotFoundException(){
        UserLoginRequest userLoginRequest = new UserLoginRequest("testId", "testPassword");
        given(userRepository.findById(anyString())).willReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUserLogin(userLoginRequest));
    }

    @Test
    @Order(8)
    @DisplayName("로그인 : 관리자 첫 로그인 시 비밀번호 변경 여부")
    void getUserLogin_AdminMustUpdatePasswordException(){
        UserLoginRequest userLoginRequest = new UserLoginRequest("testId", "testPassword");
        User user = User.builder()
                .id("testId")
                .role(new Role(1L, "ROLE_TEST"))
                .latestLoginAt(null)
                .build();

        given(userRepository.findById(anyString())).willReturn(Optional.ofNullable(user));

        Assertions.assertThrows(AdminMustUpdatePasswordException.class, () -> userService.getUserLogin(userLoginRequest));
    }

    @Test
    @Order(9)
    @DisplayName("로그인 : DB 정보와 Request 에서 받은 유저 정보 중 password 비교")
    void getUserLogin_InvalidPasswordException(){
        UserLoginRequest userLoginRequest = new UserLoginRequest("testId", "testPassword");
        User user = User.builder()
                .id("testId")
                .role(new Role(1L, "ROLE_TEST"))
                .password("testPassword")
                .latestLoginAt(LocalDateTime.now())
                .build();

        given(userRepository.findById(anyString())).willReturn(Optional.ofNullable(user));
        given(passwordEncoder.matches(any(), any())).willReturn(false);

        Assertions.assertThrows(InvalidPasswordException.class, () -> userService.getUserLogin(userLoginRequest));
    }

    @Test
    @Order(10)
    @DisplayName("유저 등록")
    void createUser() {
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setId("testId");

        given(userRepository.existsById(any(String.class))).willReturn(false);
        given(userRepository.getByEmail(null)).willReturn(Optional.empty());
        given(roleRepository.getUserRole()).willReturn(new Role(1L, "ROLE_TEST"));
        given(statusRepository.getActiveStatus()).willReturn(new Status(1L, "STATUS_TEST"));

        userService.createUser(userCreateRequest);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        then(userRepository).should().save(userArgumentCaptor.capture());
        Assertions.assertEquals("testId", userArgumentCaptor.getValue().getId());
    }

    @Test
    @Order(11)
    @DisplayName("유저 등록 : 유저가 존재하지 않을 때")
    void createUser_UserAlreadyExistException(){
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setId("testId");
        given(userRepository.existsById(anyString())).willReturn(true);

        Assertions.assertThrows(UserAlreadyExistException.class, () -> userService.createUser(userCreateRequest));
    }

    @Test
    @Order(12)
    void updateUser() {
        UserCreateRequest userCreateRequest = new UserCreateRequest("testId",
                "changeName",
                "changePassword",
                "changeEmail",
                "changeBirth");
        String userId = "testId";
        User existedUser = User.builder()
                .id("testId")
                .name("existedName")
                .email("existedEmail")
                .birth("existedBirth")
                .password("existedPassword")
                .latestLoginAt(LocalDateTime.now())
                .status(new Status(1L, "TEST_ROLE"))
                .build();

        given(userRepository.findById(any(String.class))).willReturn(Optional.ofNullable(existedUser));
        given(userRepository.getByEmail(userCreateRequest.getEmail())).willReturn(Optional.empty());
        given(passwordEncoder.encode(any(String.class))).willReturn("changePassword");
        given(statusRepository.getActiveStatus()).willReturn(new Status(1L, "TEST_ROLE"));

        userService.updateUser(userCreateRequest, userId);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        then(userRepository).should().save(userArgumentCaptor.capture());
        Assertions.assertEquals("testId", userArgumentCaptor.getValue().getId());
        Assertions.assertEquals("changePassword", userArgumentCaptor.getValue().getPassword());
        Assertions.assertEquals("changeEmail", userArgumentCaptor.getValue().getEmail());
        Assertions.assertEquals("changeBirth", userArgumentCaptor.getValue().getBirth());
        Assertions.assertEquals("TEST_ROLE", userArgumentCaptor.getValue().getStatus().getName());

    }

    @Test
    @Order(13)
    void deleteUser() {
        User user = User.builder().id("testId").build();

        given(userRepository.findById(any(String.class))).willReturn(Optional.ofNullable(user));
        given(statusRepository.getDeactivatedStatus()).willReturn(new Status(1L, "STATUS_TEST"));

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        userService.deleteUser("testId");

        then(userRepository).should().save(userArgumentCaptor.capture());
    }

}
