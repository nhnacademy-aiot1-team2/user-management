package com.user.management.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


/**
 * 액세스 토큰과 토큰 타입, 만료 시간을 저장하는 dto
 *
 * @author parksangwon
 * @version 1.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccessTokenResponse {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("expire_in")
    private Integer expiresIn;
}
