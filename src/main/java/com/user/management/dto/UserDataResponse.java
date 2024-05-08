package com.user.management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type User data response.
 *
 * @author parksangwon
 * @version 1.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDataResponse {
    private String id;
    private String name;
    private String email;
    private String roleName;
    private String statusName;
    private String password;
}
