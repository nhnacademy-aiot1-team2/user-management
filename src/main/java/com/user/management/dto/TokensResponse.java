package com.user.management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 액세스 토큰 관련 dto와 리프레쉬 토큰 관련 dto를 저장하는 dto
 *
 * @author parksangwon
 * @version 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokensResponse {
    private AccessTokenResponse accessToken;
    private RefreshTokenResponse refreshToken;
}
