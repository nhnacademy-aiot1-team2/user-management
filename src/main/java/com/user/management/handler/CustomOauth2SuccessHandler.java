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

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomOauth2SuccessHandler implements AuthenticationSuccessHandler {
    private final UserAdapter userAdapter;
    @Value("${front.url}")
    private String frontUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
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

    private static String capitalize(String inputString) {
        String first = String.valueOf(inputString.charAt(0));
        String other = inputString.substring(1);
        return first.toUpperCase() + other;
    }
}
