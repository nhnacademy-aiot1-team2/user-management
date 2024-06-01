package com.user.management.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * 리프레쉬 토큰과 만료 시간을 저장하는 dto
 *
 * @author parksangwon
 * @version 1.0.0
 */
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
