package com.user.management.service;

import com.user.management.dto.*;
import com.user.management.page.RestPage;
import org.springframework.data.domain.Pageable;

/**
 * 사용자 관련 처리를 수행하는 서비스 인터페이
 *
 * @author parksangwon
 * @version 1.0.0
 */
public interface UserService {
    /**
     * 모든 사용자를 조회하는 메서
     *
     * @param pageable the pageable
     * @return the all users
     */
    RestPage<UserDataResponse> getAllUsers(Pageable pageable);

    /**
     * 상태가 일치하는 사용자를 조회하는 메서드
     *
     * @param statusId the status id
     * @param pageable the pageable
     * @return the filtered users by status
     */
    RestPage<UserDataResponse> getFilteredUsersByStatus(Long statusId, Pageable pageable);

    /**
     * 권한이 일치하는 사용자를 조회하는 메서
     *
     * @param roleId   the role id
     * @param pageable the pageable
     * @return the filtered users by role
     */
    RestPage<UserDataResponse> getFilteredUsersByRole(Long roleId, Pageable pageable);

    /**
     * 아이디가 일치하는 사용자르 조회하는 메서드
     *
     * @param id the id
     * @return the user by id
     */
    UserDataResponse getUserById(String id);

    /**
     * 로그인 정보를 받아 해당하는 사용자 정보를 반환하는 메서드
     *
     * @param userLoginRequest the user login request
     * @return the user login
     */
    UserDataResponse getUserLogin(UserLoginRequest userLoginRequest);

    /**
     * 사용자 상태를 수정하는 메서
     *
     * @param permitUserRequest the permit user request
     */
    UserDataResponse permitUser(PermitUserRequest permitUserRequest);

    /**
     * 사용자 상태를 수정하는 메서드
     *
     * @param permitUserRequest the permit user request
     */
    UserDataResponse promoteUser(PermitUserRequest permitUserRequest);

    /**
     * 사용자를 추가하는 메서드
     *
     * @param userCreateRequest the user create request
     */
    UserDataResponse createUser(UserCreateRequest userCreateRequest);

    /**
     * 사용자를 수정하는 메서드
     *
     * @param userUpdateRequest the user update request
     * @param userId            the user id
     */
    UserDataResponse updateUser(UserUpdateRequest userUpdateRequest, String userId);

    /**
     * 사용자를 비활성화하는 메서드
     *
     * @param userId the user id
     */
    UserDataResponse deactivateUser(String userId);

    /**
     * 사용자를 제거하는 메서드`
     *
     * @param deleteUserRequest the delete user request
     */
    void deleteUser(DeleteUserRequest deleteUserRequest);

    /**
     * userId로 Role 반환
     *
     * @param id userId
     * @return RoleResponse
     */
    RoleResponse getRoleByUserId(String id);

}
