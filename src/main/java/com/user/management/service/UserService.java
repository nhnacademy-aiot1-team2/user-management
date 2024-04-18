package com.user.management.service;

import com.user.management.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface User service.
 *
 * @author parksangwon
 * @version 1.0.0
 */
public interface UserService {
    /**
     * Gets all users.
     *
     * @param pageable the pageable
     * @return the all users
     */
    Page<UserDataResponse> getAllUsers(Pageable pageable);

    /**
     * Gets filtered users by status.
     *
     * @param statusId the status id
     * @param pageable the pageable
     * @return the filtered users by status
     */
    Page<UserDataResponse> getFilteredUsersByStatus(Long statusId, Pageable pageable);

    /**
     * Gets user by id.
     *
     * @param id the id
     * @return the user by id
     */
    UserDataResponse getUserById(String id);

    /**
     * Gets user login.
     *
     * @param userLoginRequest the user login request
     * @return the user login
     */
    UserDataResponse getUserLogin(UserLoginRequest userLoginRequest);

    /**
     * Permit user.
     *
     * @param permitUserRequest the permit user request
     */
    void permitUser(PermitUserRequest permitUserRequest);

    /**
     * Promote user.
     *
     * @param permitUserRequest the permit user request
     */
    void promoteUser(PermitUserRequest permitUserRequest);

    /**
     * Create user.
     *
     * @param userCreateRequest the user create request
     */
    void createUser(UserCreateRequest userCreateRequest);

    /**
     * Update user.
     *
     * @param userUpdateRequest the user update request
     * @param userId            the user id
     */
    void updateUser(UserUpdateRequest userUpdateRequest, String userId);

    /**
     * Deactivate user.
     *
     * @param userId the user id
     */
    void deactivateUser(String userId);

    /**
     * Delete user.
     *
     * @param deleteUserRequest the delete user request
     */
    void deleteUser(DeleteUserRequest deleteUserRequest);

}
