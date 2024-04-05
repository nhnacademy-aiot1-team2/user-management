package com.user.management.service.impl;

import com.user.management.dto.*;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;


    @Mock
    private StatusRepository statusRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ProviderRepository providerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers() {
        Pageable pageable = PageRequest.of(0, 10);

        UserDataResponse userDataResponse = new UserDataResponse();
        Page<UserDataResponse> expectedPage = new PageImpl<>(List.of(userDataResponse));
        when(userRepository.getAllUserData(pageable)).thenReturn(expectedPage);

        Page<UserDataResponse> page = userService.getAllUsers(pageable);
        assertEquals(expectedPage.getSize(), page.getSize());
        assertEquals(expectedPage.getContent().get(0).getId(), page.getContent().get(0).getId());
    }

    @Test
    public void getFilteredUsersByStatus() {
        Page<UserDataResponse> page = new PageImpl<>(new ArrayList<>());

        when(statusRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.getUsersFilteredByStatusId(any(), anyLong())).thenReturn(page);

        Page<UserDataResponse> result = userService.getFilteredUsersByStatus(1L, Pageable.ofSize(10));

        assertEquals(page, result);

        verify(statusRepository, times(1)).existsById(anyLong());
        verify(userRepository, times(1)).getUsersFilteredByStatusId(any(), anyLong());
    }

    @Test
    void getUserById() {
        final String TEST_USER_ID = "testUserId";
        UserDataResponse expectedUser = new UserDataResponse();
        expectedUser.setId("testUserId");

        when(userRepository.getUserById(TEST_USER_ID)).thenReturn(Optional.of(expectedUser));

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
                .password("testPassword")
                .role(new Role(1L, "ROLE_ADMIN"))
                .status(new Status(1L, "ACTIVE"))
                .createdAt(LocalDateTime.now())
                .build();

        when(userRepository.findById(userLoginRequest.getId())).thenReturn(Optional.of(mockedUser));
        when(passwordEncoder.matches(any(), any())).thenReturn(true); // passwordEncoder 는 Mock 객체라 null 이 반환된다.

        assertThrows(AdminMustUpdatePasswordException.class, () -> {
            userService.getUserLogin(userLoginRequest);
        });

        User updatedUser = mockedUser.toBuilder()
                .latestLoginAt(LocalDateTime.now())
                .build();

        when(userRepository.findById(userLoginRequest.getId())).thenReturn(Optional.of(updatedUser));
        UserDataResponse response = userService.getUserLogin(userLoginRequest);

        assertEquals(updatedUser.getId(), response.getId());
        assertEquals(updatedUser.getRole().getName(), response.getRoleName());
        assertEquals(updatedUser.getPassword(), response.getPassword());
    }

    @Test
    void createUser() {
        UserCreateRequest userCreateRequest =
                new UserCreateRequest("testId", "testName", "testPassword", "test@gmail.com");

        User expectedUser = User.builder()
                .id(userCreateRequest.getId())
                .email(userCreateRequest.getEmail())
                .build();

        userService.createUser(userCreateRequest);

        verify(userRepository, times(1)).existsById(userCreateRequest.getId());
        verify(userRepository, times(1)).save(any(User.class));

        when(userRepository.existsById(userCreateRequest.getId())).thenReturn(false);
        when(userRepository.getByEmail(userCreateRequest.getEmail())).thenReturn(Optional.of(expectedUser));
        assertThrows(AlreadyExistEmailException.class, () -> {
            userService.createUser(userCreateRequest);
        });
    }


    @Test
    void permitUser() {
        String testId = "testId";

        User user = User.builder()
                .id(testId)
                .build();

        PermitUserRequest permitUserRequest = new PermitUserRequest(user.getId());

        when(statusRepository.getActiveStatus()).thenReturn(new Status(1L, "Active"));
        when(userRepository.findById(testId)).thenReturn(Optional.of(user));
        userService.permitUser(permitUserRequest);

        verify(userRepository, times(1)).save(any(User.class));
    }


    @Test
    void promoteUser() {
        String testId = "testId";
        Role adminRole = new Role(1L, "ROLE_ADMIN");
        Role userRole = new Role(2L, "ROLE_USER");

        User user = User.builder()
                .id(testId)
                .role(userRole)
                .build();

        when(userRepository.findById(testId)).thenReturn(Optional.of(user));
        when(roleRepository.getAdminRole()).thenReturn(adminRole);

        PermitUserRequest permitUserRequest = new PermitUserRequest(user.getId());

        userService.promoteUser(permitUserRequest);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals(testId, savedUser.getId());
        assertEquals(adminRole, savedUser.getRole());

        verify(userRepository, times(1)).save(any(User.class));
        verify(userRepository, times(1)).findById(testId);
        verify(roleRepository, times(1)).getAdminRole();
    }

    @Test
    void updateUser() {
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
                .password(passwordEncoder.encode("testPassword"))
                .latestLoginAt(LocalDateTime.now())
                .status(activeStatus)
                .role(role)
                .createdAt(LocalDateTime.now())
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(originalUser));
        when(userRepository.getByEmail(newEmail)).thenReturn(Optional.empty());
        when(statusRepository.getActiveStatus()).thenReturn(activeStatus);

        userService.updateUser(updateRequest, userId);

        // LocalDateTime.now()가 목 객체에서 오류를 일으켜서 ArgumentCaptor<User>로 문제되지 않게 변경
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();

        assertEquals(updateRequest.getName(), savedUser.getName());
        assertEquals(newEmail, savedUser.getEmail());
        assertEquals(passwordEncoder.encode(updateRequest.getPassword()), savedUser.getPassword());

        when(userRepository.getByEmail(newEmail)).thenReturn(Optional.of(originalUser));
        assertThrows(AlreadyExistEmailException.class, () -> userService.updateUser(updateRequest, userId));

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(updateRequest, userId));
    }

    @Test
    void deactivateUser() {
        String userId = "testId";

        User originalUser = User.builder()
                .id(userId)
                .status(statusRepository.getActiveStatus())
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(originalUser));

        userService.deactivateUser(userId);

        verify(userRepository, times(1)).findById(userId);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.deactivateUser(userId));
    }

    @Test
    void updateUserInactivityStatus() {
        Status inActiveStatus = new Status(2L, "INACTIVE");
        Status activeStatus = new Status(1L, "ACTIVE");

        User activeUserOne = User.builder()
                .id("testIdOne")
                .latestLoginAt(LocalDateTime.now().minusMonths(2))
                .status(activeStatus)
                .build();

        User activeUserTwo = User.builder()
                .id("testIdTwo")
                .latestLoginAt(LocalDateTime.now().minusWeeks(1))
                .status(new Status(1L, "ACTIVE"))
                .build();

        when(userRepository.findAll()).thenReturn(Arrays.asList(activeUserOne, activeUserTwo));
        when(statusRepository.getInActiveStatus()).thenReturn(inActiveStatus);

        userService.updateUserInactivityStatus();

        // 2번째 테스트는 최근 로그인이 1주일 전이므로, update가 진행되지 않음.
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());
        List<User> savedUsers = userCaptor.getAllValues();

        // 1번째 user는 마지막 로그인이 2달전이니, 휴면 상태로 전환된다.
        assertEquals(inActiveStatus, savedUsers.get(0).getStatus());
    }

    @Test
    void checkAndUpdateInactivity() {
        User activeUser = User.builder()
                .id("testId")
                .latestLoginAt(LocalDateTime.now().minusMonths(2))
                .status(new Status(1L, "ACTIVE"))
                .build();

        Status inActiveStatus = new Status(2L, "INACTIVE");
        when(statusRepository.getInActiveStatus()).thenReturn(inActiveStatus);

        userService.checkAndUpdateInactivity(activeUser);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals(inActiveStatus, savedUser.getStatus());
    }
}