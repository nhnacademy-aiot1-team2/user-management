package com.user.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
