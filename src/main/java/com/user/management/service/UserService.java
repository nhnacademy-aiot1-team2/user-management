package com.user.management.service;

import com.user.management.dto.UserCreateRequest;
import com.user.management.dto.UserDataResponse;
import com.user.management.dto.UserLoginRequest;
import com.user.management.entity.User;

import java.util.List;

public interface UserService {
    public List<UserDataResponse> getAllUsers(String id);

    public UserDataResponse getUserById(String id);
    public User getUserLogin(UserLoginRequest userLoginRequest);

    void createUser(UserCreateRequest userCreateRequest);

    public void updateUser(UserCreateRequest userCreateRequest, String userId);

    void deleteUser(String id);

}
