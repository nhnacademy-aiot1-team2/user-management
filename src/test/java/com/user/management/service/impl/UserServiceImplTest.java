package com.user.management.service.impl;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;


    @Mock
    private StatusRepository statusRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

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

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.getRoleByUserId(userId)).thenReturn(roleAdmin);
        when(userRepository.getAllUserData(pageable)).thenReturn(expectedPage);

        Page<UserDataResponse> page = userService.getAllUsers(userId, pageable);

        assertEquals(expectedPage.getSize(), page.getSize());
        assertEquals(expectedPage.getContent().get(0).getId(), page.getContent().get(0).getId());
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
                new UserCreateRequest("testId", "testName", "testPassword", "test@gmail.com", "19991102");

        Role testRole = new Role(1L, "ROLE_USER");
        Status testStatus = new Status(1L, "ACTIVE");

        User expectedUser = User.builder()
                .id(userCreateRequest.getId())
                .name(userCreateRequest.getName())
                .email(userCreateRequest.getEmail())
                .birth(userCreateRequest.getBirth())
                .password(userCreateRequest.getPassword())
                .role(testRole)
                .status(testStatus)
                .createdAt(LocalDateTime.now())
                .latestLoginAt(LocalDateTime.now())
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
    void updateUser() {
        String userId = "testId";
        String userEmail = "testuser@test.com";
        String newEmail = "newuser@test.com";

        // 패스워드 변경 상태 확인을 위해, passwordEncode 메소드를 사용하지 않음.
        UserCreateRequest updateRequest =
                new UserCreateRequest("testId", "newName", "newPassword", newEmail, "19991102");


        Status activeStatus = new Status(1L, "ACTIVE");
        Role role = new Role(2L, "ROLE_USER");

        User originalUser = User.builder()
                .id(userId)
                .name("testName")
                .email(userEmail)
                .birth("19801102")
                .password("testPassword") // 패스워드 변경 상태 확인을 위해, passwordEncode 메소드를 사용하지 않음.
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
        assertEquals(updateRequest.getBirth(), savedUser.getBirth());
        assertEquals(passwordEncoder.encode(updateRequest.getPassword()), savedUser.getPassword());

        when(userRepository.getByEmail(newEmail)).thenReturn(Optional.of(originalUser));
        assertThrows(AlreadyExistEmailException.class, () -> userService.updateUser(updateRequest, userId));

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(updateRequest, userId));
    }

    @Test
    void deleteUser() {
        String userId = "testId";

        Status deactivatedStatus = statusRepository.getDeactivatedStatus();
        Role role = roleRepository.getUserRole();

        User originalUser = User.builder()
                .id(userId)
                .name("testName")
                .birth("19801102")
                .password(passwordEncoder.encode("password"))
                .latestLoginAt(LocalDateTime.now())
                .status(statusRepository.getActiveStatus())
                .role(role)
                .createdAt(LocalDateTime.now())
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(originalUser));
        when(statusRepository.getDeactivatedStatus()).thenReturn(deactivatedStatus); // null == null

        userService.deleteUser(userId);

        verify(userRepository, times(1)).findById(userId);

        // LocalDateTime.now()가 목 객체에서 오류를 일으켜서 ArgumentCaptor<User>로 문제되지 않게 변경
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userId));
    }

    @Test
    void updateUserInactivityStatus() {
        String userIdOne = "testIdOne";
        Status activeStatusOne = new Status(1L, "ACTIVE");
        Role roleOne = new Role(2L, "ROLE_USER");
        LocalDateTime loginTimeOne = LocalDateTime.now().minusMonths(2);

        User activeUserOne = User.builder()
                .id(userIdOne)
                .name("testNameOne")
                .birth("19851102")
                .password("testPassword")
                .latestLoginAt(loginTimeOne)
                .status(activeStatusOne)
                .role(roleOne)
                .createdAt(LocalDateTime.now())
                .build();

        String userIdTwo = "testIdTwo";
        Status activeStatusTwo = new Status(1L, "ACTIVE");
        LocalDateTime loginTimeTwo = LocalDateTime.now().minusWeeks(1);

        User activeUserTwo = User.builder()
                .id(userIdTwo)
                .name("testNameTwo")
                .birth("19851102")
                .password("testPassword")
                .latestLoginAt(loginTimeTwo)
                .status(activeStatusTwo)
                .role(roleOne)
                .createdAt(LocalDateTime.now())
                .build();

        when(userRepository.findAll()).thenReturn(Arrays.asList(activeUserOne, activeUserTwo));

        Status inActiveStatus = new Status(2L, "INACTIVE");
        when(statusRepository.getInActiveStatus()).thenReturn(inActiveStatus);

        userService.updateUserInactivityStatus();

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture()); // 2번째 테스트는 최근 로그인이 1주일 전이므로, update가 진행되지 않음.
        List<User> savedUsers = userCaptor.getAllValues();

        assertEquals(inActiveStatus, savedUsers.get(0).getStatus()); // 1번째 user는 마지막 로그인이 2달전이니, 휴면 상태로 전환된다.
    }

    @Test
    void checkAndUpdateInactivity() {
        String userId = "testId";
        Status activeStatus = new Status(1L, "ACTIVE");
        Role role = new Role(2L, "ROLE_USER");
        LocalDateTime loginTime = LocalDateTime.now().minusMonths(2);

        User activeUser = User.builder()
                .id(userId)
                .name("testName")
                .birth("19851102")
                .password("testPassword")
                .latestLoginAt(loginTime)
                .status(activeStatus)
                .role(role)
                .createdAt(LocalDateTime.now())
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