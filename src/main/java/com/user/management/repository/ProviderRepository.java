package com.user.management.repository;

import com.user.management.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Provider 엔티티 인스턴스를 관리하기 위한 레포지토리입니다.
 */
public interface ProviderRepository extends JpaRepository<Provider, String> {

    /**
     * 제공된 이름과 일치하는 Provider를 검색합니다.
     *
     * @param name 검색하고자 하는 Provider의 이름.
     * @return 이름이 대응하는 Optional<Provider>.
     */
    Optional<Provider> findByName(String name);

    /**
     * "Default"라는 ID를 가진 기본 Provider를 가져옵니다.
     *
     * @return "Default" ID로 검색된 Provider. 만약 결과가 없다면, null이 반환됩니다.
     */
    default Provider getDefaultProvider() {
        return findById("Default").orElse(null);
    }
}