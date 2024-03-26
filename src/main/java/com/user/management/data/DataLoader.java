package com.user.management.data;

import com.user.management.entity.Status;
import com.user.management.entity.Role;
import com.user.management.repository.StatusRepository;
import com.user.management.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final StatusRepository statusRepository;
    private final RoleRepository roleRepository;

    public DataLoader(StatusRepository statusRepository, RoleRepository roleRepository) {
        this.statusRepository = statusRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        statusRepository.save(new Status(1L, "ACTIVE"));
        statusRepository.save(new Status(2L, "INACTIVE"));
        statusRepository.save(new Status(3L, "DEACTIVATE"));

        roleRepository.save(new Role(1L, "ROLE_ADMIN"));
        roleRepository.save(new Role(2L, "ROLE_USER"));
    }
}
