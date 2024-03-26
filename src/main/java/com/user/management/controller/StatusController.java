package com.user.management.controller;


import com.user.management.entity.Status;
import com.user.management.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/status")
@RequiredArgsConstructor
public class StatusController {
    private final StatusRepository statusRepository;
    @PostMapping("/1")
    public ResponseEntity<Void> createActiveStatus()
    {
      statusRepository.save(new Status(1L, "ACTIVE"));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/2")
    public ResponseEntity<Void> createInActiveStatus()
    {
        statusRepository.save(new Status(2L, "INACTIVE"));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/3")
    public ResponseEntity<Void> createDeactivateStatus()
    {
        statusRepository.save(new Status(3L, "DEACTIVATE"));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
