package com.user.management.repository;

import com.user.management.config.JasyptConfig;
import com.user.management.dto.UserDataResponse;
import com.user.management.entity.Role;
import com.user.management.entity.Status;
import com.user.management.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(JasyptConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserRepository userRepository;

    @Test
    void getRoleByUserId() {
        String roleName = "test role";
        String userId = "test user id";

        Role role = Role.builder()
                .name(roleName)
                .build();
        User user = User.builder()
                .id(userId)
                .role(role)
                .build();

        entityManager.persist(role);
        entityManager.persist(user);

        Long roleId = role.getId();
        Role resultRole = userRepository.getRoleByUserId(userId);

        assertAll(
                () -> assertNotNull(resultRole),
                () -> assertEquals(roleId, resultRole.getId()),
                () -> assertEquals(roleName, resultRole.getName())
        );
    }

    @Test
    void getAllUserData() {
        String roleName = "test role";
        String statusName = "test status";
        String userId = "test user id";
        String userName = "test user";
        String email = "test email";
        String password = "test password";
        Pageable pageable = PageRequest.of(0, 10);

        Role role = Role.builder()
                .name(roleName)
                .build();
        Status status = Status.builder()
                .name(statusName)
                .build();
        User user = User.builder()
                .id(userId)
                .name(userName)
                .email(email)
                .password(password)
                .role(role)
                .status(status)
                .build();

        entityManager.persist(role);
        entityManager.persist(status);
        entityManager.persist(user);

        Page<UserDataResponse> userPage = userRepository.getAllUserData(pageable);

        assertAll(
                () -> assertNotNull(userPage),
                () -> assertFalse(userPage.isEmpty())
        );
    }

    @Test
    void getUsersFilteredByStatusId() {
        String roleName = "test role";
        String statusName = "test status";
        String userId = "test user id";
        String userName = "test user";
        String email = "test email";
        String password = "test password";
        Pageable pageable = PageRequest.of(0, 10);

        Role role = Role.builder()
                .name(roleName)
                .build();
        Status status = Status.builder()
                .name(statusName)
                .build();
        User user = User.builder()
                .id(userId)
                .name(userName)
                .email(email)
                .password(password)
                .role(role)
                .status(status)
                .build();

        entityManager.persist(role);
        entityManager.persist(status);
        entityManager.persist(user);

        Long statusId = status.getId();
        Page<UserDataResponse> userPage = userRepository.getUsersFilteredByStatusId(pageable, statusId);

        assertAll(
                () -> assertNotNull(userPage),
                () -> assertFalse(userPage.isEmpty())
        );
    }

    @Test
    void getUserById() {
        String roleName = "test role";
        String statusName = "test status";
        String userId = "test user id";
        String userName = "test user";
        String email = "test email";
        String password = "test password";

        Role role = Role.builder()
                .name(roleName)
                .build();
        Status status = Status.builder()
                .name(statusName)
                .build();
        User user = User.builder()
                .id(userId)
                .name(userName)
                .email(email)
                .password(password)
                .role(role)
                .status(status)
                .build();

        entityManager.persist(role);
        entityManager.persist(status);
        entityManager.persist(user);

        Optional<UserDataResponse> userOptional = userRepository.getUserById(userId);
        UserDataResponse userDataResponse = userOptional.orElseThrow();

        assertAll(
                () -> assertNotNull(userOptional),
                () -> assertNotNull(userDataResponse),
                () -> assertEquals(userId, userDataResponse.getId()),
                () -> assertEquals(userName, userDataResponse.getName()),
                () -> assertEquals(email, userDataResponse.getEmail()),
                () -> assertEquals(roleName, userDataResponse.getRoleName()),
                () -> assertEquals(statusName, userDataResponse.getStatusName()),
                () -> assertEquals(password, userDataResponse.getPassword())
        );
    }

    @Test
    void getByEmail() {
        String roleName = "test role";
        String statusName = "test status";
        String userId = "test user id";
        String userName = "test user";
        String email = "test email";
        String password = "test password";

        Role role = Role.builder()
                .name(roleName)
                .build();
        Status status = Status.builder()
                .name(statusName)
                .build();
        User user = User.builder()
                .id(userId)
                .name(userName)
                .email(email)
                .password(password)
                .role(role)
                .status(status)
                .build();

        entityManager.persist(role);
        entityManager.persist(status);
        entityManager.persist(user);

        Optional<User> userOptional = userRepository.getByEmail(email);
        User resultUser = userOptional.orElseThrow();

        assertAll(
                () -> assertNotNull(userOptional),
                () -> assertNotNull(resultUser),
                () -> assertEquals(userId, resultUser.getId()),
                () -> assertEquals(userName, resultUser.getName()),
                () -> assertEquals(email, resultUser.getEmail()),
                () -> assertEquals(role, resultUser.getRole()),
                () -> assertEquals(status, resultUser.getStatus()),
                () -> assertEquals(password, resultUser.getPassword())
        );
    }
}