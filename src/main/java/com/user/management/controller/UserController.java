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

/**
 * 사용자 관리를 위한 REST API 컨트롤러
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 사용자 ID를 기반으로 모든 사용자 정보를 검색하는 메소드
     * @param id 사용자 ID
     * @return DB에 저장된 모든 UserList
     * @throws UserHeaderNotFoundException X-USER-ID header가 존재하지 않는 경우에 발생
     */
    @GetMapping
    public ResponseEntity<List<User>> findAllUsers(@RequestHeader(value = "X-USER-ID", required = false) String id)
    {
        if(id == null)
            throw new UserHeaderNotFoundException();

        return ResponseEntity.ok().body(userService.getAllUsers(id));
    }

    /**
     * 사용자 ID를 기반으로 특정 사용자 정보를 조회하는 메소드
     * @param id 사용자 ID
     * @return 검색된 사용자 정보
     * @throws UserHeaderNotFoundException X-USER-ID header가 존재하지 않는 경우에 발생
     */
    @GetMapping("/myPage")
    public ResponseEntity<User> findUser(@RequestHeader(value = "X-USER-ID", required = false) String id)
    {
        if(id == null)
            throw new UserHeaderNotFoundException();

        return ResponseEntity.ok().body(userService.getUserById(id));
    }

    /**
     * 새로운 사용자를 등록하는 메소드
     * @param userCreateRequest 사용자 생성 요청에 필요한 데이터 (id, name, password, email, birth)
     * @return 상태 코드 201 (생성됨)
     */
    @PostMapping("/register")
    public ResponseEntity<Void> createUser(@RequestBody UserCreateRequest userCreateRequest)
    {
        userService.createUser(userCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 사용자 로그인 메소드
     * @param userLoginRequest 사용자 로그인 요청에 필요한 데이터 (id, password)
     * @return 로그인한 사용자 정보
     */
    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody UserLoginRequest userLoginRequest)
    {
        return ResponseEntity.ok().body(userService.getUserLogin(userLoginRequest));
    }

    /**
     * 사용자 정보를 업데이트하는 메소드
     * @param userCreateRequest 사용자 갱신 요청에 필요한 데이터 (id, name, password, email, birth)
     * @param id 사용자 ID
     * @return 상태 코드 204 (내용 없음)
     * @throws UserHeaderNotFoundException X-USER-ID header가 존재하지 않는 경우에 발생
     */
    @PutMapping("/update")
    public ResponseEntity<Void> updateUser(@RequestBody UserCreateRequest userCreateRequest, @RequestHeader(value = "X-USER-ID", required = false) String id)
    {
        if(id == null)
            throw new UserHeaderNotFoundException();

        userService.updateUser(userCreateRequest, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 사용자의 status 정보를 DEACTIVATE로 변경 (사용자 데이터 유지)
     * @param id 사용자 ID
     * @return 상태 코드 204 (내용 없음)
     * @throws UserHeaderNotFoundException X-USER-ID header가 존재하지 않는 경우에 발생
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(@RequestHeader(value = "X-USER-ID", required = false) String id)
    {
        if(id == null)
            throw new UserHeaderNotFoundException();

        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}