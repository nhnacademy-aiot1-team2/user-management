package com.user.management.controller;

import com.user.management.dto.UserCreateRequest;
import com.user.management.dto.UserLoginRequest;
import com.user.management.entity.User;
import com.user.management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<User> findUser(@RequestHeader("X-USER-ID") String id)
    {
        return ResponseEntity.ok().body(userService.getUserById(id));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> createUser(@RequestBody UserCreateRequest userCreateRequest)
    {
        userService.createUser(userCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody UserLoginRequest userLoginRequest)
    {
        return ResponseEntity.ok().body(userService.getUserLogin(userLoginRequest));
    }

    @PutMapping
    public ResponseEntity<Void> updateUser(@RequestBody UserCreateRequest userCreateRequest, @RequestHeader("X-USER-ID") String id)
    {
        userService.updateUser(userCreateRequest, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestHeader("X-USER-ID") String id)
    {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

