package com.user.management.repository;

import com.user.management.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 역할(Role)에 대한 데이터를 접근하는 레포지터리입니다.
 * JpaRepository를 상속 받아 기본적인 CRUD 기능을 제공합니다.
 * Author : jjunho50
 */
public interface RoleRepository extends JpaRepository<Role,Long> {

    /**
     * role_id(1) : 관리자
     *
     * @return 관리자 역할. (ROLE_ADMIN) , 예외 발생 가능성 x
     */
    default Role getAdminRole()
    {
        return findById(1L).orElse(null);
    }

    /**
     * role_id(2) : 유저
     *
     * @return 사용자 역할. (ROLE_USER) , 예외 발생 가능성 x
     */
    default Role getUserRole()
    {
        return findById(2L).orElse(null);
    }
}