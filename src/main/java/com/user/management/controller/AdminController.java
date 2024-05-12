package com.user.management.controller;


import com.user.management.dto.DeleteUserRequest;
import com.user.management.dto.PermitUserRequest;
import com.user.management.dto.UserDataResponse;
import com.user.management.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 관리자 관련 API controller
 *
 * @author parksangwon
 * @version 1.0.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/admin")
@Tag(name = "Admin Rest Controller", description = "관리자 관련 API")
public class AdminController {
    private final UserService userService;

    /**
     * 모든 사용자 정보를 조회하는 메서드입니다.
     *
     * @param page 페이징 정보 (어떤 페이지를 조회할지) 를 제공하는 매개변수입니다.
     * @param size 한 페이지에 얼마나 많은 항목을 보여줄지를 결정하는 매개변수입니다.
     * @return 사용자 정보의 부분 리스트를 담은 ResponseEntity를 돌려줍니다.
     */
    @GetMapping("/userList")
    @Operation(summary = "모든 사용자를 조회")
    public ResponseEntity<Page<UserDataResponse>> findAllUsers(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "5", required = false) int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDataResponse> userPage = userService.getAllUsers(pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(userPage);
    }

    /**
     * 특정 status를 가진 사용자의 정보를 페이지 별로 조회합니다.
     *
     * @param page     반환할 페이지 번호.
     * @param size     한 페이지에 포함될 항목 수.
     * @param statusId 검색할 사용자 상태 ID. // 1. 기본 , 2. 휴면, 3. 비활성화, 4. 승인대기
     * @return UserDataResponse 리스트를 래핑한 ResponseEntity를 반환합니다.
     */
    @GetMapping("/userList/sort/status/{statusId}")
    @Operation(summary = "특정 상태를 가진 사용자를 조회")
    public ResponseEntity<Page<UserDataResponse>> findSortedUserByStatus(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "5", required = false) int size,
            @PathVariable Long statusId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDataResponse> userPage = userService.getFilteredUsersByStatus(statusId, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(userPage);
    }

    /**
     * 특정 role을 가진 사용자의 정보를 페이지 별로 조회합니다.
     *
     * @param page   반환할 페이지 번호.
     * @param size   한 페이지에 포함될 항목 수.
     * @param roleId 검색할 사용자 role ID. // 1. 어드민, 2. 유저
     * @return UserDataResponse 리스트를 래핑한 ResponseEntity를 반환합니다.
     */
    @GetMapping("/userList/sort/role/{roleId}")
    @Operation(summary = "특정 권한을 가진 사용자를 조히")
    public ResponseEntity<Page<UserDataResponse>> findSortedUserByRole(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "5", required = false) int size,
            @PathVariable Long roleId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDataResponse> userPage = userService.getFilteredUsersByRole(roleId, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(userPage);
    }

    /**
     * 일반 사용자를 관리자로 진급시킵니다.
     *
     * @param permitUserRequest 관리자로 진급시킬 사용자의 정보를 포함하는 요청 본문.
     * @return 상태 코드 204를 포함하는 응답 엔티티를 반환합니다.
     */
    @PostMapping("/promotion")
    @Operation(summary = "사용자의 권한을 관리자로 업데이트")
    public ResponseEntity<Void> promoteUserToAdmin(
            @RequestBody @Valid PermitUserRequest permitUserRequest) {
        userService.promoteUser(permitUserRequest);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    /**
     * 승인 대기 중인 사용자에게 일반 권한을 부여합니다
     *
     * @param permitUserRequestList 권한을 부여할 사용자의 정보를 포함하는 요청 본문.
     * @return 상태 코드 204를 포함하는 응답 엔티티를 반환합니다.
     */
    @PostMapping("/permit")
    @Operation(summary = "승인 대기 사용자를 일반 사용자로 업데이트")
    public ResponseEntity<Void> permitUser(@RequestBody @Valid List<PermitUserRequest> permitUserRequestList) {
        permitUserRequestList.parallelStream()
                .forEach(userService::permitUser);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }


    /**
     * 비활성인 사용자를 활성으로 변경합니다.
     *
     * @param deleteUserRequestList 비활성 사용자 아이디 리스트
     * @return 상태 코드 204를 포함하는 응답 엔티티를 반환합니다.
     */
    @PostMapping("/reject/delete")
    @Operation(summary = "비활성 사용자를 활성 상태로 업데이트")
    public ResponseEntity<Void> rejectDeleteUser(@RequestBody @Valid List<DeleteUserRequest> deleteUserRequestList) {
        deleteUserRequestList.stream()
                .map(request -> new PermitUserRequest(request.getId()))
                .forEach(userService::permitUser);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    /**
     * 사용자 데이터를 완전히 삭제합니다.
     *
     * @param deleteUserRequestList 삭제할 사용자의 리스트 정보를 포함하는 요청 본문.
     * @return 상태 코드 204를 포함하는 응답 엔티티를 반환합니다.
     */
    @DeleteMapping("/delete")
    @Operation(summary = "사용자를 제거")
    public ResponseEntity<Void> deleteUser(@RequestBody @Valid List<DeleteUserRequest> deleteUserRequestList) {
        deleteUserRequestList.parallelStream()
                .forEach(userService::deleteUser);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
