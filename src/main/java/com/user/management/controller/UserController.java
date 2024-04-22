package com.user.management.controller;

import com.user.management.dto.*;
import com.user.management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 사용자 관리를 위한 REST API 컨트롤러
 *
 * @author jjunho50
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 사용자 ID를 기반으로 특정 사용자 정보를 조회하는 메소드
     *
     * @param id 사용자 ID
     * @return 검색된 사용자 정보
     */
    @GetMapping("/myPage")
    public ResponseEntity<UserDataResponse> findUser(@RequestHeader(value = "X-USER-ID", required = false) String id) {
        return ResponseEntity.ok().body(userService.getUserById(id));
    }

    /**
     * 새로운 사용자를 등록하는 메소드
     *
     * @param userCreateRequest 사용자 생성 요청에 필요한 데이터 (id, name, password, email)
     * @return 상태 코드 201 (생성됨)
     */
    @PostMapping("/register")
    public ResponseEntity<Void> createUser(@RequestBody UserCreateRequest userCreateRequest) {
        userService.createUser(userCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 사용자 로그인 메소드
     *
     * @param userLoginRequest 사용자 로그인 요청에 필요한 데이터 (id, password)
     * @return 로그인한 사용자 정보
     */
    @PostMapping("/login")
    public ResponseEntity<UserDataResponse> loginUser(@RequestBody UserLoginRequest userLoginRequest) {
        return ResponseEntity.ok().body(userService.getUserLogin(userLoginRequest));
    }

    /**
     * 사용자 정보를 업데이트하는 메소드
     *
     * @param userUpdateRequest 사용자 갱신 요청에 필요한 데이터 (id, name, password, email)
     * @param id                사용자 ID
     * @return 상태 코드 204 (내용 없음)
     */
    @PutMapping("/update")
    public ResponseEntity<Void> updateUser(@RequestBody UserUpdateRequest userUpdateRequest, @RequestHeader(value = "X-USER-ID", required = false) String id) {
        userService.updateUser(userUpdateRequest, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 사용자의 status 정보를 DEACTIVATE 로 변경 (사용자 데이터 유지)
     * 본인은 데이터를 삭제할 수 없고, ADMIN이 삭제할 수 있다.
     *
     * @param id 사용자 ID
     * @return 상태 코드 204 (내용 없음)
     */
    @PostMapping("/deactivate")
    public ResponseEntity<Void> deactivateUser(@RequestHeader(value = "X-USER-ID", required = false) String id) {
        userService.deactivateUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/role")
    public ResponseEntity<RoleResponse> getRoleId(@RequestHeader(value = "X-USER-ID") String id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getRoleByUserId(id));
    }
}