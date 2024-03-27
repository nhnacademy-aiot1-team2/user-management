package com.user.management.service;

import com.user.management.dto.UserCreateRequest;
import com.user.management.dto.UserDataResponse;
import com.user.management.dto.UserLoginRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    public Page<UserDataResponse> getAllUsers(String id, Pageable pageable);

    public UserDataResponse getUserById(String id);
    public UserDataResponse getUserLogin(UserLoginRequest userLoginRequest);

    void createUser(UserCreateRequest userCreateRequest);

    public void updateUser(UserCreateRequest userCreateRequest, String userId);

    void deleteUser(String id);

}
