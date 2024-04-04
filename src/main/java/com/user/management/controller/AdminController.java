package com.user.management.controller;


import com.user.management.dto.DeleteUserRequest;
import com.user.management.dto.PermitUserRequest;
import com.user.management.dto.UserDataResponse;
import com.user.management.exception.UserHeaderNotFoundException;
import com.user.management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    /**
     * 모든 사용자 정보를 조회하는 메서드입니다.
     *
     * @param id   사용자 ID를 포함하는 'X-USER-ID' 요청 헤더.
     * @param page 페이징 정보 (어떤 페이지를 조회할지) 를 제공하는 매개변수입니다.
     * @param size 한 페이지에 얼마나 많은 항목을 보여줄지를 결정하는 매개변수입니다.
     * @return 사용자 정보의 부분 리스트를 담은 ResponseEntity를 돌려줍니다.
     */
    @GetMapping("/userList")
    public ResponseEntity<List<UserDataResponse>> findAllUsers(
            @RequestHeader(value = "X-USER-ID", required = false) String id,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "5", required = false) int size) {


        Pageable pageable = PageRequest.of(page, size);
        Page<UserDataResponse> userPage = userService.getAllUsers(id, pageable);

        return ResponseEntity.ok().body(userPage.getContent());
    }

    /**
     * 특정 상태를 가진 사용자의 정보를 페이지 별로 조회합니다.
     *
     * @param page     반환할 페이지 번호.
     * @param size     한 페이지에 포함될 항목 수.
     * @param statusId 검색할 사용자 상태 ID. // 1. 기본 , 2. 휴면, 3. 비활성화, 4. 승인대기
     * @return UserDataResponse 리스트를 래핑한 ResponseEntity를 반환합니다.
     */
    @GetMapping("/userList/sort/{statusId}")
    public ResponseEntity<List<UserDataResponse>> findSortedUser(
            @RequestHeader(value = "X-USER-ID", required = false) String adminUserId,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "5", required = false) int size,
            @PathVariable Long statusId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDataResponse> userPage = userService.getFilteredUsersByStatus(statusId, pageable, adminUserId);

        return ResponseEntity.ok().body(userPage.getContent());
    }

    /**
     * 일반 사용자를 관리자로 진급시킵니다.
     *
     * @param permitUserRequest 관리자로 진급시킬 사용자의 정보를 포함하는 요청 본문.
     * @return 상태 코드 204를 포함하는 응답 엔티티를 반환합니다.
     */
    @PostMapping("/promotion")
    public ResponseEntity<Void> promoteUserToAdmin(
            @RequestBody PermitUserRequest permitUserRequest, @RequestHeader(value = "X-USER-ID", required = false) String adminUserId) {
        userService.promoteUser(permitUserRequest, adminUserId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 승인 대기 중인 사용자에게 일반 권한을 부여합니다
     *
     * @param permitUserRequest 권한을 부여할 사용자의 정보를 포함하는 요청 본문.
     * @return 상태 코드 204를 포함하는 응답 엔티티를 반환합니다.
     */
    @PostMapping("/permit")
    public ResponseEntity<Void> permitUser(@RequestBody PermitUserRequest permitUserRequest,  @RequestHeader(value = "X-USER-ID", required = false) String adminUserId) {
        userService.permitUser(permitUserRequest, adminUserId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 사용자 데이터를 완전히 삭제합니다.
     *
     * @param deleteUserRequest 삭제할 사용자의 정보를 포함하는 요청 본문.
     * @return 상태 코드 204를 포함하는 응답 엔티티를 반환합니다.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(@RequestBody DeleteUserRequest deleteUserRequest, @RequestHeader(value = "X-USER-ID", required = false) String adminUserId) {
        userService.deleteUser(deleteUserRequest.getDeleteUserId(), adminUserId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
