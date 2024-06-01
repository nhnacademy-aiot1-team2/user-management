package com.user.management.repository;

import com.user.management.config.JasyptConfig;
import com.user.management.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(JasyptConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    void getAdminRole() {
        Long roleId = 1L;
        String roleName = "ROLE_ADMIN";

        Role adminRole = roleRepository.getAdminRole();

        assertAll(
                () -> assertNotNull(adminRole),
                () -> assertEquals(roleId, adminRole.getId()),
                () -> assertEquals(roleName, adminRole.getName())
        );
    }

    @Test
    void getUserRole() {
        Long roleId = 2L;
        String roleName = "ROLE_USER";

        Role adminRole = roleRepository.getUserRole();

        assertAll(
                () -> assertNotNull(adminRole),
                () -> assertEquals(roleId, adminRole.getId()),
                () -> assertEquals(roleName, adminRole.getName())
        );
    }
}