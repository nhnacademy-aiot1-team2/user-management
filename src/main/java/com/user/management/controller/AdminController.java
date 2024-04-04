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
     * @throws UserHeaderNotFoundException X-USER-ID header 가 존재하지 않는 경우에 발생
     */
    @GetMapping("/userList")
    public ResponseEntity<List<UserDataResponse>> findAllUsers(
            @RequestHeader(value = "X-USER-ID", required = false) String id,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "5", required = false) int size) {
        if (id == null)
            throw new UserHeaderNotFoundException();

        Pageable pageable = PageRequest.of(page, size);
        Page<UserDataResponse> userPage = userService.getAllUsers(id, pageable);

        return ResponseEntity.ok().body(userPage.getContent());
    }


    @GetMapping("/userList/sort/{statusId}")
    public ResponseEntity<List<UserDataResponse>> findSortedUser(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "5", required = false) int size,
            @PathVariable Long statusId)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDataResponse> userPage = userService.getFilteredUsersByStatus(statusId , pageable);

        return ResponseEntity.ok().body(userPage.getContent());
    }

    @PostMapping("/promotion")
    public ResponseEntity<Void> promoteUserToAdmin(@RequestBody PermitUserRequest permitUserRequest)
    {
        userService.promoteUser(permitUserRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/permit")
    public ResponseEntity<Void> permitUser(@RequestBody PermitUserRequest permitUserRequest)
    {
        userService.permitUser(permitUserRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(@RequestBody DeleteUserRequest deleteUserRequest) {
        userService.deleteUser(deleteUserRequest.getDeleteUserId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
