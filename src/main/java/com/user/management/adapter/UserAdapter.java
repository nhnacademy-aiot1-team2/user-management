package com.user.management.adapter;

import com.user.management.dto.LoginRequest;
import com.user.management.dto.TokensResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * User 관련 기능을 수행하는 FeignClient 인터페이스
 */
@FeignClient(value = "authorization-server", path = "/api/auth/login")
public interface UserAdapter {
    /**
     * 사용자 로그인
     *
     * @param loginRequest 로그인 요청 정보를 담은 객체
     * @return TokensResponse 객체, 로그인 결과에 대한 정보
     */
    @PostMapping
    TokensResponse doLogin(LoginRequest loginRequest);

}
