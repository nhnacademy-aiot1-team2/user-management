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
 * 사용자(User)에 대한 데이터를 접근하는 레포지터리입니다.
 * JpaRepository를 상속 받아 기본적인 CRUD 기능을 제공합니다.
 */
public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT u.role FROM User u WHERE u.id = :id")
    Role getRoleByUserId(@Param("id") String userId);

    @Query("SELECT new com.user.management.dto.UserDataResponse(u.id, u.name, u.email, u.birth, u.role.name, u.status.name) FROM User u")
    List<UserDataResponse> getAllUserData();

    @Query("SELECT new com.user.management.dto.UserDataResponse(u.id, u.name, u.email, u.birth, u.role.name, u.status.name) FROM User u WHERE u.id = :id")
    Optional<UserDataResponse> getUserById(@Param("id") String userId);
}