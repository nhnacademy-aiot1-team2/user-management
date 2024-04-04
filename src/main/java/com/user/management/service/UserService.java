package com.user.management.service;

import com.user.management.dto.PermitUserRequest;
import com.user.management.dto.UserCreateRequest;
import com.user.management.dto.UserDataResponse;
import com.user.management.dto.UserLoginRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    public Page<UserDataResponse> getAllUsers(String id, Pageable pageable);
    Page<UserDataResponse> getFilteredUsersByStatus(Long statusId, Pageable pageable);

    public UserDataResponse getUserById(String id);

    public UserDataResponse getUserLogin(UserLoginRequest userLoginRequest);
    void permitUser(PermitUserRequest permitUserRequest);
    void promoteUser(PermitUserRequest permitUserRequest);

    void createUser(UserCreateRequest userCreateRequest);

    public void updateUser(UserCreateRequest userCreateRequest, String userId);

    void deactivateUser(String id);

    void deleteUser(String id);

}
