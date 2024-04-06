package com.user.management.service;

import com.user.management.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserDataResponse> getAllUsers(Pageable pageable);

    Page<UserDataResponse> getFilteredUsersByStatus(Long statusId, Pageable pageable);
    Page<UserDataResponse> getFilteredUsersByRole(Long roleId, Pageable pageable);

    UserDataResponse getUserById(String id);

    UserDataResponse getUserLogin(UserLoginRequest userLoginRequest);

    void permitUser(PermitUserRequest permitUserRequest);

    void promoteUser(PermitUserRequest permitUserRequest);

    void createUser(UserCreateRequest userCreateRequest);

    void updateUser(UserUpdateRequest userUpdateRequest, String userId);

    void deactivateUser(String userId);

    void deleteUser(DeleteUserRequest deleteUserRequest);

}
