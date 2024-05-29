package com.user.management.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshTokenResponse {
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("expire_in")
    private Integer expiresIn;
}
