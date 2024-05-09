package com.user.management.repository;

import com.user.management.config.JasyptConfig;
import com.user.management.entity.Provider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(JasyptConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProviderRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ProviderRepository providerRepository;

    @Test
    void findByName() {
        String providerId = "test provider id";
        String providerName = "test provider";
        Provider provider = Provider.builder()
                .id(providerId)
                .name(providerName)
                .build();

        entityManager.persist(provider);

        Optional<Provider> providerOptional = providerRepository.findByName(providerName);
        Provider resultProvider = providerOptional.orElseThrow();

        assertAll(
                () -> assertNotNull(providerOptional),
                () -> assertNotNull(resultProvider),
                () -> assertEquals(providerId, resultProvider.getId()),
                () -> assertEquals(providerName, resultProvider.getName())
        );
    }

    @Test
    void getDefaultProvider() {
        String providerId = "Default";
        String providerName = "default provider";
        Provider provider = Provider.builder()
                .id(providerId)
                .name(providerName)
                .build();

        entityManager.persist(provider);

        Provider resultProvider = providerRepository.getDefaultProvider();

        assertAll(
                () -> assertNotNull(resultProvider),
                () -> assertEquals(providerId, resultProvider.getId()),
                () -> assertEquals(providerName, resultProvider.getName())
        );
    }
}