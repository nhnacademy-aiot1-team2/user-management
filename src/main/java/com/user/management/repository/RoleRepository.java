package com.user.management.repository;

import com.user.management.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long > {

    default Role getAdminRole()
    {
        return findById(1L).orElse(null);
    }
    default Role getUserRole()
    {
        return findById(2L).orElse(null);
    }
}
