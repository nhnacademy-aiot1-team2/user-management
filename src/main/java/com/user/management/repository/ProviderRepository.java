package com.user.management.repository;

import com.user.management.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProviderRepository extends JpaRepository<Provider, String> {
    Optional<Provider> findByName(String name);

    default Provider getDefaultProvider()
    {
        return findById("Default").orElse(null);
    }
}
