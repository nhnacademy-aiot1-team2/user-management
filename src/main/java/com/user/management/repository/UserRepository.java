package com.user.management.repository;

import com.user.management.dto.UserDataResponse;
import com.user.management.entity.Role;
import com.user.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 사용자(User) 정보를 관리하는 레포지터리 인터페이스입니다.
 */
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * 주어진 ID에 해당하는 사용자의 Role 을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 사용자 Role
     */
    @Query("SELECT u.role FROM User u WHERE u.id = :id")
    Role getRoleByUserId(@Param("id") String userId);

    /**
     * 모든 사용자 정보를 조회하여 반환합니다.
     *
     * @return List<UserDataResponse>
     */
    @Query("SELECT new com.user.management.dto.UserDataResponse(u.id, u.name, u.email, u.birth, u.role.name, u.status.name) FROM User u")
    List<UserDataResponse> getAllUserData();

    /**
     * 주어진 ID에 해당하는 사용자 정보를 조회하여 반환합니다.
     *
     * @param userId 사용자 ID
     * @return Optional<UserDataResponse>
     */
    @Query("SELECT new com.user.management.dto.UserDataResponse(u.id, u.name, u.email, u.birth, u.role.name, u.status.name) FROM User u WHERE u.id = :id")
    Optional<UserDataResponse> getUserById(@Param("id") String userId);
}