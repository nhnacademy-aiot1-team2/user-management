package com.user.management.service.impl;

import com.user.management.dto.UserCreateRequest;
import com.user.management.dto.UserLoginRequest;
import com.user.management.entity.Role;
import com.user.management.entity.User;
import com.user.management.exception.AlreadyExistEmailException;
import com.user.management.exception.InvalidPasswordException;
import com.user.management.exception.UserAlreadyExistException;
import com.user.management.exception.UserNotFoundException;
import com.user.management.repository.ProviderRepository;
import com.user.management.repository.RoleRepository;
import com.user.management.repository.StatusRepository;
import com.user.management.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class UserServiceImplExceptionTest {
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
    void getAllUsers_UserNotFoundTest() {
        Pageable pageable = PageRequest.of(0, 10);

        when(userRepository.getAllUserData(pageable)).thenReturn(new PageImpl<>(Collections.emptyList()));
        assertThrows(UserNotFoundException.class, () -> userService.getAllUsers(pageable));
    }

    @Test
    void getUserById_NotFoundException() {
        String notExistedUserId = "notExisted";

        when(userRepository.existsById(notExistedUserId)).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(notExistedUserId));
    }

    @Test
    void getUserLogin_InvalidPasswordException() {
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
    void createUser_UserAlreadyExistException() {
        UserCreateRequest userCreateRequest =
                new UserCreateRequest("testId", "testName", "testPassword", "test@gmail.com");

        when(userRepository.existsById(userCreateRequest.getId())).thenReturn(true);

        assertThrows(UserAlreadyExistException.class, () -> {
            userService.createUser(userCreateRequest);
        });
    }

    @Test
    void createUser_AlreadyExistEmailException() {
        UserCreateRequest userCreateRequest =
                new UserCreateRequest("testId", "testName", "testPassword", "test@gmail.com");

        User expectedUser = User.builder()
                .id(userCreateRequest.getId())
                .name(userCreateRequest.getName())
                .email(userCreateRequest.getEmail())
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
