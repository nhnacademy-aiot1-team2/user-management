package com.user.management.service;

import com.user.management.dto.UserCreateRequest;
import com.user.management.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();

    User getUserById(String id);

    void createUser(UserCreateRequest userCreateRequest);

    public void updateUser(UserCreateRequest userCreateRequest, String userId);

    void deleteUser(String id);

}
