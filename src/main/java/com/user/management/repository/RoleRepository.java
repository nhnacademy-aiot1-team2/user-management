package com.user.management.repository;

import com.user.management.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 역할(Role)에 대한 데이터를 접근하는 레포지터리입니다.
 * JpaRepository를 상속 받아 기본적인 CRUD 기능을 제공합니다.
 */
public interface RoleRepository extends JpaRepository<Role,Long> {

    /**
     * 관리자 역할을 찾아 반환하는 메소드입니다.
     * 관리자는 id가 1인 역할로 간주됩니다.
     *
     * @return 관리자 역할. 없을 경우 null을 반환합니다. (하지만 초기 데이터 덕에 null이 반환될 일은 없다)
     */
    default Role getAdminRole()
    {
        return findById(1L).orElse(null);
    }

    /**
     * 사용자 역할을 찾아 반환하는 메소드입니다.
     * 사용자는 id가 2인 역할로 간주됩니다.
     *
     * @return 사용자 역할. 없을 경우 null을 반환하지만, 초기 데이터 덕에 null이 반환될 일은 없다)
     */
    default Role getUserRole()
    {
        return findById(2L).orElse(null);
    }
}