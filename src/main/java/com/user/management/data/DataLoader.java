package com.user.management.data;

import com.user.management.entity.Status;
import com.user.management.entity.Role;
import com.user.management.entity.User;
import com.user.management.repository.StatusRepository;
import com.user.management.repository.RoleRepository;
import com.user.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private static final String ADMIN_ID = "admin";

    private final StatusRepository statusRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;


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
            roleRepository.save(new Role(1L, "ROLE_ADMIN")); // 어드민
        }
        if (!roleRepository.existsById(2L)) {
            roleRepository.save(new Role(2L, "ROLE_USER")); // 유저
        }


        if(!userRepository.existsById(ADMIN_ID))
        {
            userRepository.save(User.builder()
                    .id(ADMIN_ID)
                    .password(ADMIN_ID)
                    .name(ADMIN_ID)
                    .createdAt(LocalDateTime.now())
                    .latestLoginAt(LocalDateTime.now())
                    .birth("12341122")
                    .email("admin@example.com")
                    .status(statusRepository.getActiveStatus())
                    .role(roleRepository.getAdminRole())
                    .build());
        }
    }
}
