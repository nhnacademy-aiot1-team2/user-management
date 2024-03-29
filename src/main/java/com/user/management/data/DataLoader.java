package com.user.management.data;

import com.user.management.entity.Status;
import com.user.management.entity.Role;
import com.user.management.entity.User;
import com.user.management.repository.StatusRepository;
import com.user.management.repository.RoleRepository;
import com.user.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 데이터 로더 클래스, 서버 시작 시 초기 데이터를 주입합니다.
 * {@link CommandLineRunner} 인터페이스를 구현하여 파라미터로 전달된 문자열 배열을 처리하는 run 메소드를 오버라이드합니다.
 */
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private static final String ADMIN = "admin";

    private final StatusRepository statusRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 핵심 비즈니스 로직을 실행하는 메소드.
     * 사용자의 Status, Role 에 대한 초기 데이터를 설정하고, 관리자 계정을 생성합니다.
     * @param args 문자열 배열
     * @throws Exception 예외 처리를 위한 Exception 클래스 적용
     */
    @Override
    public void run(String... args) throws Exception {
        if (!statusRepository.existsById(1L)) {
            statusRepository.save(new Status(1L, "ACTIVE")); // 기본 상태
        }
        if (!statusRepository.existsById(2L)) {
            statusRepository.save(new Status(2L, "INACTIVE")); // 휴면 상태
        }
        if (!statusRepository.existsById(3L)) {
            statusRepository.save(new Status(3L, "DEACTIVATE")); // 탈퇴 상태
        }

        if (!roleRepository.existsById(1L)) {
            roleRepository.save(new Role(1L, "ROLE_ADMIN")); // 관리자
        }
        if (!roleRepository.existsById(2L)) {
            roleRepository.save(new Role(2L, "ROLE_USER")); // 사용자
        }

        if(!userRepository.existsById(ADMIN))
        {

            userRepository.save(User.builder()
                    .id(ADMIN)
                    .password(passwordEncoder.encode(ADMIN))
                    .name(ADMIN)
                    .createdAt(LocalDateTime.now()) // 계정 생성 시각
                    .latestLoginAt(null) //
                    .birth("12341122") // 생년월일
                    .email("admin@example.com") // 이메일
                    .status(statusRepository.getActiveStatus()) // 상태는 'ACTIVE'(활성)
                    .role(roleRepository.getAdminRole()) // 역할은 'ROLE_ADMIN'(관리자)
                    .build());
        }
    }
}