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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceImplExceptionTest {
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
    void getAllUsers_UserNotFoundTest()
    {
        String userId = "invalidId";
        Pageable pageable = PageRequest.of(0, 10);

        when(userRepository.existsById(userId)).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> userService.getAllUsers(userId, pageable));
    }

    @Test
    void getAllUsers_NotAnAdmin() {
        String userId = "RoleUserId";
        Pageable pageable = PageRequest.of(0, 10);

        Role roleUser = new Role(2L, "ROLE_USER");

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.getRoleByUserId(userId)).thenReturn(roleUser);

        assertThrows(OnlyAdminCanAccessUserDataException.class, () -> userService.getAllUsers(userId, pageable));
    }

    @Test
    void getUserById_NotFoundException()

    {
        String notExistedUserId = "notExisted";

        when(userRepository.existsById(notExistedUserId)).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(notExistedUserId));
    }

    @Test
    void getUserLogin_InvalidPasswordException()
    {
        String userId = "userId";
        String INVALID_PASSWORD = "222";
        String VALID_PASSWORD = "111";

        UserLoginRequest userLoginRequest = new UserLoginRequest(userId, INVALID_PASSWORD);

        User user = User.builder()
                .id(userId)
                .password(VALID_PASSWORD)
                .latestLoginAt(LocalDateTime.now())
                .role(new Role(2L, "ROLE_USER"))
                .build();

        when(userRepository.findById(userLoginRequest.getId())).thenReturn(Optional.of(user));
        assertThrows(InvalidPasswordException.class, () -> userService.getUserLogin(userLoginRequest));
    }

    @Test
    void createUser_UserAlreadyExistException()
    {
        UserCreateRequest userCreateRequest =
                new UserCreateRequest("testId", "testName", "testPassword", "test@gmail.com", "19991102");

        userService.createUser(userCreateRequest);

        when(userRepository.existsById(userCreateRequest.getId())).thenReturn(true);
        assertThrows(UserAlreadyExistException.class, () -> {
            userService.createUser(userCreateRequest);
        });
    }

    @Test
    void createUser_AlreadyExistEmailException()
    {
        UserCreateRequest userCreateRequest =
                new UserCreateRequest("testId", "testName", "testPassword", "test@gmail.com", "19991102");

        User expectedUser = User.builder()
                .id(userCreateRequest.getId())
                .name(userCreateRequest.getName())
                .email(userCreateRequest.getEmail())
                .birth(userCreateRequest.getBirth())
                .password(userCreateRequest.getPassword())
                .latestLoginAt(LocalDateTime.now())
                .build();

        when(userRepository.existsById(userCreateRequest.getId())).thenReturn(false);
        when(userRepository.getByEmail(userCreateRequest.getEmail())).thenReturn(Optional.of(expectedUser));
        assertThrows(AlreadyExistEmailException.class, () -> {
            userService.createUser(userCreateRequest);
        });
    }


}
