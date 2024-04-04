package com.user.management.service;

import com.user.management.dto.PermitUserRequest;
import com.user.management.dto.UserCreateRequest;
import com.user.management.dto.UserDataResponse;
import com.user.management.dto.UserLoginRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserDataResponse> getAllUsers(String adminUserId, Pageable pageable);

    Page<UserDataResponse> getFilteredUsersByStatus(Long statusId, Pageable pageable, String adminUserId);

    UserDataResponse getUserById(String id);

    UserDataResponse getUserLogin(UserLoginRequest userLoginRequest);

    void permitUser(PermitUserRequest permitUserRequest, String adminUserId);

    void promoteUser(PermitUserRequest permitUserRequest, String adminUserId);

    void createUser(UserCreateRequest userCreateRequest);

    void updateUser(UserCreateRequest userCreateRequest, String userId);

    void deactivateUser(String userId);

    void deleteUser(String id, String adminUserId);

}
