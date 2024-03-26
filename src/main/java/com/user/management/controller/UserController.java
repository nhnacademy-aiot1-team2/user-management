package com.user.management.controller;

import com.user.management.dto.UserCreateRequest;
import com.user.management.dto.UserLoginRequest;
import com.user.management.entity.User;
import com.user.management.exception.UserHeaderNotFoundException;
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
    public ResponseEntity<List<User>> findAllUsers(@RequestHeader(value = "X-USER-ID", required = false) String id)
    {
        if(id == null)
            throw new UserHeaderNotFoundException();

        return ResponseEntity.ok().body(userService.getAllUsers(id));
    }

    @GetMapping("/myPage")
    public ResponseEntity<User> findUser(@RequestHeader(value = "X-USER-ID", required = false) String id)
    {
        if(id == null)
            throw new UserHeaderNotFoundException();

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
    public ResponseEntity<Void> updateUser(@RequestBody UserCreateRequest userCreateRequest, @RequestHeader(value = "X-USER-ID", required = false) String id)
    {
        if(id == null)
            throw new UserHeaderNotFoundException();

        userService.updateUser(userCreateRequest, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestHeader(value = "X-USER-ID", required = false) String id)
    {
        if(id == null)
            throw new UserHeaderNotFoundException();

        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

