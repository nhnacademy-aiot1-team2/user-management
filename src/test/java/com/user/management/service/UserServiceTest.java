package com.user.management.service;

import com.user.management.dto.UserDataResponse;
import com.user.management.entity.Role;
import com.user.management.repository.RoleRepository;
import com.user.management.repository.StatusRepository;
import com.user.management.repository.UserRepository;
import com.user.management.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

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
    @Order(4)
    void createUser() {

//        Mockito.when(roleRepository.getUserRole()).thenReturn(new Role(1L, "ROLE_TEST"));
//        Mocki
//
    }

}
