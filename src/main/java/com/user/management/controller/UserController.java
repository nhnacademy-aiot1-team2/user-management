package com.user.management.controller;

import com.user.management.dto.UserCreateRequest;
import com.user.management.dto.UserDataResponse;
import com.user.management.dto.UserLoginRequest;
import com.user.management.exception.UserHeaderNotFoundException;
import com.user.management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
     * 'X-USER-ID' userId로 관리자의 요청인지 식별
     *
     * @param id       'X-USER-ID' 헤더의 사용자 ID, 이 값이 없으면 예외 발생
     * @param pageable 페이징 정보, 기본값은 page=0&size=5 (설정은 application-prod.properties 참조)
     * @return         사용자 정보를 포함하는 리스트, 응답은 UserDataResponse 리스트 형태로 반환
     */
    @GetMapping
    public ResponseEntity<List<UserDataResponse>> findAllUsers(@RequestHeader(value = "X-USER-ID", required = false) String id, Pageable pageable)
    {
        if(id == null)
            throw new UserHeaderNotFoundException();

        Page<UserDataResponse> userPage = userService.getAllUsers(id, pageable);

        return ResponseEntity.ok().body(userPage.getContent());
    }

    /**
     * 사용자 ID를 기반으로 특정 사용자 정보를 조회하는 메소드
     * @param id 사용자 ID
     * @return 검색된 사용자 정보
     * @throws UserHeaderNotFoundException X-USER-ID header 가 존재하지 않는 경우에 발생
     */
    @GetMapping("/myPage")
    public ResponseEntity<UserDataResponse> findUser(@RequestHeader(value = "X-USER-ID", required = false) String id)
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
    public ResponseEntity<UserDataResponse> loginUser(@RequestBody UserLoginRequest userLoginRequest)
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