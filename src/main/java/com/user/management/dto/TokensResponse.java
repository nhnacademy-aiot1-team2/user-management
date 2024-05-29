package com.user.management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokensResponse {
    private AccessTokenResponse accessToken;
    private RefreshTokenResponse refreshToken;
}
