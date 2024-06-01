package com.user.management.handler;

import com.user.management.adapter.UserAdapter;
import com.user.management.dto.AccessTokenResponse;
import com.user.management.dto.LoginRequest;
import com.user.management.dto.RefreshTokenResponse;
import com.user.management.dto.TokensResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Oauth 로그인 성공시 필요한 작업을 수행하는 랜들러 클래스
 *
 * @author parksangwon
 * @version 1.0.0
 */
@Component
@RequiredArgsConstructor
public class CustomOauth2SuccessHandler implements AuthenticationSuccessHandler {
    private final UserAdapter userAdapter;
    @Value("${front.url}")
    private String frontUrl;

    /**
     * Oauth 로그인 성공 시 인증 서버에서 액세스 토큰과 리프레시 토큰을 가져와서 쿠키로 만들고 응답에 넣는 역할을 수행하는 메서드
     *
     * @param request        요청
     * @param response       응답
     * @param authentication 인증 정보
     * @throws IOException 입력 출력 예외
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2AuthenticationToken oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        String userName = capitalize(oauth2AuthenticationToken.getAuthorizedClientRegistrationId().concat("_").concat(authentication.getName()));

        LoginRequest loginRequest = new LoginRequest(userName, userName);
        TokensResponse tokensResponse = userAdapter.doLogin(loginRequest);
        AccessTokenResponse accessTokenResponse = tokensResponse.getAccessToken();
        RefreshTokenResponse refreshTokenResponse = tokensResponse.getRefreshToken();

        Cookie accessCookie = new Cookie("accessToken", accessTokenResponse.getAccessToken());
        Cookie refreshCookie = new Cookie("refreshToken", refreshTokenResponse.getRefreshToken());

        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        response.sendRedirect(frontUrl);
    }

    /**
     * provider의 첫 글자를 대문자로 바꿔주는 메서드
     *
     * @param inputString provider + 인증 name
     * @return 변환된 이름
     */
    private static String capitalize(String inputString) {
        String first = String.valueOf(inputString.charAt(0));
        String other = inputString.substring(1);
        return first.toUpperCase() + other;
    }
}
