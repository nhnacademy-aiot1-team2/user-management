package com.user.management.controller;

import com.user.management.entity.Role;
import com.user.management.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleRepository roleRepository;

    @PostMapping("/admin")
    public ResponseEntity<Void> createAdminRole()
    {
        roleRepository.save(new Role("ROLE_ADMIN"));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/user")
    public ResponseEntity<Void> createUserRole()
    {
        roleRepository.save(new Role("ROLE_USER"));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
