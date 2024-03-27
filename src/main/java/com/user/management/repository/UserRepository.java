package com.user.management.repository;

import com.user.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * 사용자(User)에 대한 데이터를 접근하는 레포지터리입니다.
 * JpaRepository를 상속 받아 기본적인 CRUD 기능을 제공합니다.
 */
public interface UserRepository extends JpaRepository<User, String> {
    /**
     * 추후에 front나 개발 프로젝트가 정해지면 엔티티가 아닌 Dto를 사용할 예정
     */
}