package com.user.management.service.impl;

import com.user.management.dto.UserCreateRequest;
import com.user.management.dto.UserDataResponse;
import com.user.management.dto.UserLoginRequest;
import com.user.management.dto.UserUpdateRequest;
import com.user.management.entity.Role;
import com.user.management.entity.Status;
import com.user.management.entity.User;
import com.user.management.exception.AdminMustUpdatePasswordException;
import com.user.management.exception.AlreadyExistEmailException;
import com.user.management.exception.UserNotFoundException;
import com.user.management.repository.ProviderRepository;
import com.user.management.repository.RoleRepository;
import com.user.management.repository.StatusRepository;
import com.user.management.repository.UserRepository;
import com.user.management.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@WebMvcTest(UserService.class)
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private StatusRepository statusRepository;
    @MockBean
    private RoleRepository roleRepository;
    @MockBean
    private ProviderRepository providerRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void getAllUsers_AdminAccess() {
        String userId = "testId";
        Pageable pageable = PageRequest.of(0, 10);

        Role roleAdmin = new Role(1L, "ROLE_ADMIN");
        User adminUser = User.builder()
                .id(userId)
                .role(roleAdmin)
                .build();

        UserDataResponse userDataResponse = new UserDataResponse();
        userDataResponse.setId(adminUser.getId());
        userDataResponse.setRoleName(adminUser.getRole().getName());

        Page<UserDataResponse> expectedPage = new PageImpl<>(List.of(userDataResponse));

        given(userRepository.existsById(userId)).willReturn(true);
        given(userRepository.getRoleByUserId(userId)).willReturn(roleAdmin);
        given(userRepository.getAllUserData(pageable)).willReturn(expectedPage);

        Page<UserDataResponse> page = userService.getAllUsers(pageable);

        assertEquals(expectedPage.getSize(), page.getSize());
        assertEquals(expectedPage.getContent().get(0).getId(), page.getContent().get(0).getId());
    }

    @Test
    void getUserById() {
        final String TEST_USER_ID = "testUserId";
        UserDataResponse expectedUser = new UserDataResponse();
        expectedUser.setId("testUserId");

        given(userRepository.getUserById(TEST_USER_ID)).willReturn(Optional.of(expectedUser));

        UserDataResponse actualUser = userService.getUserById(TEST_USER_ID);

        assertNotNull(actualUser);
        assertEquals(expectedUser, actualUser);
        verify(userRepository, times(1)).getUserById(TEST_USER_ID);
    }

    @Test
    void getUserLogin() {
        UserLoginRequest userLoginRequest = new UserLoginRequest("testId", "testPassword");
        User mockedUser = User.builder()
                .id(userLoginRequest.getId())
                .name("test user")
                .password("testPassword")
                .role(new Role(1L, "ROLE_ADMIN"))
                .status(new Status(1L, "ACTIVE"))
                .createdAt(LocalDateTime.now())
                .build();

        given(userRepository.findById(userLoginRequest.getId())).willReturn(Optional.of(mockedUser));
        given(passwordEncoder.matches(any(), any())).willReturn(true); // passwordEncoder 는 Mock 객체라 null 이 반환된다.

        assertThrows(AdminMustUpdatePasswordException.class, () -> {
            userService.getUserLogin(userLoginRequest);
        });

        User updatedUser = mockedUser.toBuilder()
                .latestLoginAt(LocalDateTime.now())
                .build();

        given(userRepository.findById(userLoginRequest.getId())).willReturn(Optional.of(updatedUser));
        given(passwordEncoder.matches(any(), any())).willReturn(true);
        given(userRepository.save(any())).willReturn(updatedUser);
        UserDataResponse response = userService.getUserLogin(userLoginRequest);

        assertEquals(updatedUser.getId(), response.getId());
        assertEquals(updatedUser.getRole().getName(), response.getRoleName());
        assertEquals(updatedUser.getPassword(), response.getPassword());
    }

    @Test
    void createUser() {
        UserCreateRequest userCreateRequest =
                new UserCreateRequest("testId", "testName", "testPassword", "test@gmail.com");

        Role testRole = new Role(1L, "ROLE_USER");
        Status testStatus = new Status(1L, "ACTIVE");

        User expectedUser = User.builder()
                .id(userCreateRequest.getId())
                .name(userCreateRequest.getName())
                .email(userCreateRequest.getEmail())
                .password(userCreateRequest.getPassword())
                .role(testRole)
                .status(testStatus)
                .createdAt(LocalDateTime.now())
                .latestLoginAt(LocalDateTime.now())
                .build();

        given(userRepository.existsById(userCreateRequest.getId())).willReturn(false);
        given(userRepository.getByEmail(userCreateRequest.getEmail())).willReturn(Optional.empty());
        given(userRepository.save(any())).willReturn(expectedUser);
        userService.createUser(userCreateRequest);

        verify(userRepository, times(1)).existsById(userCreateRequest.getId());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_Basic() {
        String userId = "testId";
        String userEmail = "testuser@test.com";
        String newEmail = "newuser@test.com";

        // 패스워드 변경 상태 확인을 위해, passwordEncode 메소드를 사용하지 않음.
        UserUpdateRequest updateRequest =
                new UserUpdateRequest("newName", "newPassword", newEmail);

        Status activeStatus = new Status(1L, "ACTIVE");
        Role role = new Role(2L, "ROLE_USER");

        User originalUser = User.builder()
                .id(userId)
                .name("testName")
                .email(userEmail)
                .password(passwordEncoder.encode("testPassword")) // 패스워드 변경 상태 확인을 위해, passwordEncode 메소드를 사용하지 않음.
                .latestLoginAt(LocalDateTime.now())
                .status(activeStatus)
                .role(role)
                .createdAt(LocalDateTime.now())
                .build();

        given(userRepository.findById(userId)).willReturn(Optional.of(originalUser));
        given(userRepository.getByEmail(newEmail)).willReturn(Optional.empty());
        given(statusRepository.getActiveStatus()).willReturn(activeStatus);
        given(userRepository.save(any())).willReturn(originalUser);

        userService.updateUser(updateRequest, userId);

        // LocalDateTime.now()가 목 객체에서 오류를 일으켜서 ArgumentCaptor<User>로 문제되지 않게 변경
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();

        assertEquals(updateRequest.getName(), savedUser.getName());
        assertEquals(newEmail, savedUser.getEmail());
        assertEquals(passwordEncoder.encode(updateRequest.getPassword()), savedUser.getPassword());

        given(userRepository.getByEmail(newEmail)).willReturn(Optional.of(originalUser));
        assertThrows(AlreadyExistEmailException.class, () -> userService.updateUser(updateRequest, userId));

        given(userRepository.findById(userId)).willReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(updateRequest, userId));
    }

    @Test
    void deleteUser() {
        String userId = "test";
        Long roleId = 1L;
        String roleName = "test role";
        Long statusId = 1L;
        String statusName = "ACTIVE";
        Long deactivateStatusId = 1L;
        String deactivateStatusName = "DEACTIVE";

        Role role = Role.builder()
                .id(roleId)
                .name(roleName)
                .build();
        Status deactivateStatus = Status.builder()
                .id(deactivateStatusId)
                .name(deactivateStatusName)
                .build();
        Status status = Status.builder()
                .id(statusId)
                .name(statusName)
                .build();
        User existedUser = User.builder()
                .id(userId)
                .role(role)
                .status(status)
                .build();

        given(userRepository.findById(anyString()))
                .willReturn(Optional.of(existedUser));
        given(statusRepository.getDeactivatedStatus())
                .willReturn(deactivateStatus);
        given(userRepository.save(any()))
                .willReturn(existedUser);

        UserDataResponse userDataResponse = userService.deactivateUser(userId);

        assertAll(
                () -> assertNotNull(userDataResponse),
                () -> assertEquals(userId, userDataResponse.getId()),
                () -> assertEquals(roleName, userDataResponse.getRoleName()),
                () -> assertEquals(statusName, userDataResponse.getStatusName())
        );
    }

    @Test
    void updateUserInactivityStatus() {
        Status inActiveStatus = new Status(2L, "INACTIVE");
        Status activeStatus = new Status(1L, "ACTIVE");
        Role role = Role.builder()
                .id(1L)
                .name("ROLE_USER")
                .build();

        User activeUserOne = User.builder()
                .id("testIdOne")
                .role(role)
                .latestLoginAt(LocalDateTime.now().minusMonths(2))
                .status(activeStatus)
                .build();

        User activeUserTwo = User.builder()
                .id("testIdTwo")
                .role(role)
                .latestLoginAt(LocalDateTime.now().minusWeeks(1))
                .status(new Status(1L, "ACTIVE"))
                .build();

        given(userRepository.findAll()).willReturn(Arrays.asList(activeUserOne, activeUserTwo));
        given(statusRepository.getInActiveStatus()).willReturn(inActiveStatus);

        userService.updateUserInactivityStatus();

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture()); // 2번째 테스트는 최근 로그인이 1주일 전이므로, update가 진행되지 않음.
        List<User> savedUsers = userCaptor.getAllValues();

        assertEquals(inActiveStatus, savedUsers.get(0).getStatus()); // 1번째 user는 마지막 로그인이 2달전이니, 휴면 상태로 전환된다.
    }

    @Test
    void checkAndUpdateInactivity() {
        Role role = Role.builder()
                .id(1L)
                .name("ROLE_USER")
                .build();
        User activeUser = User.builder()
                .id("testId")
                .role(role)
                .latestLoginAt(LocalDateTime.now().minusMonths(2))
                .status(new Status(1L, "ACTIVE"))
                .build();

        Status inActiveStatus = new Status(2L, "INACTIVE");
        given(statusRepository.getInActiveStatus()).willReturn(inActiveStatus);

        userService.checkAndUpdateInactivity(activeUser);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals(inActiveStatus, savedUser.getStatus());
    }
}