package com.user.management.repository;

import com.user.management.config.JasyptConfig;
import com.user.management.entity.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(JasyptConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StatusRepositoryTest {
    @Autowired
    private StatusRepository statusRepository;

    @Test
    void getActiveStatus() {
        Long statusId = 1L;
        String statusName = "ACTIVE";

        Status activeStatus = statusRepository.getActiveStatus();

        assertAll(
                () -> assertNotNull(activeStatus),
                () -> assertEquals(statusId, activeStatus.getId()),
                () -> assertEquals(statusName, activeStatus.getName())
        );
    }

    @Test
    void getInActiveStatus() {
        Long statusId = 2L;
        String statusName = "INACTIVE";

        Status activeStatus = statusRepository.getInActiveStatus();

        assertAll(
                () -> assertNotNull(activeStatus),
                () -> assertEquals(statusId, activeStatus.getId()),
                () -> assertEquals(statusName, activeStatus.getName())
        );
    }

    @Test
    void getDeactivatedStatus() {
        Long statusId = 3L;
        String statusName = "DEACTIVATE";

        Status activeStatus = statusRepository.getDeactivatedStatus();

        assertAll(
                () -> assertNotNull(activeStatus),
                () -> assertEquals(statusId, activeStatus.getId()),
                () -> assertEquals(statusName, activeStatus.getName())
        );
    }

    @Test
    void getPendingStatus() {
        Long statusId = 4L;
        String statusName = "PENDING";

        Status activeStatus = statusRepository.getPendingStatus();

        assertAll(
                () -> assertNotNull(activeStatus),
                () -> assertEquals(statusId, activeStatus.getId()),
                () -> assertEquals(statusName, activeStatus.getName())
        );
    }
}