package com.user.management.controller;

import com.user.management.entity.Role;
import com.user.management.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleRepository roleRepository;

    @PostMapping("/1")
    public ResponseEntity<Void> createAdminRole()
    {
        roleRepository.save(new Role(1L, "ROLE_ADMIN"));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/2")
    public ResponseEntity<Void> createUserRole()
    {
        roleRepository.save(new Role(2L, "ROLE_USER"));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
