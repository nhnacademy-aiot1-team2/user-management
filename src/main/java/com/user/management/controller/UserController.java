package com.user.management.controller;

import com.user.management.dto.UserCreateRequest;
import com.user.management.entity.User;
import com.user.management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> findAllUsers()
    {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @GetMapping("/myPage")
    public ResponseEntity<Optional<User>> findUser(@RequestHeader("X-USER-ID") String id)
    {
        return ResponseEntity.ok().body(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserCreateRequest userCreateRequest)
    {
        userService.createUser(userCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Void> updateUser(@RequestBody UserCreateRequest userCreateRequest)
    {
        userService.updateUser(userCreateRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestBody String id)
    {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}

