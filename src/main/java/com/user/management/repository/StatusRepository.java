package com.user.management.repository;

import com.user.management.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {


    // 1. 일반 회원
    default Status getActiveStatus() {
        return findById(1L).orElse(null);
    }

    // 2. 휴면 회원
    default Status getInActiveStatus()
    {
        return findById(2L).orElse(null);
    }

    // 3. 탈퇴 회원
    default Status getDeactivatedStatus()
    {
        return findById(3L).orElse(null);
    }
}
