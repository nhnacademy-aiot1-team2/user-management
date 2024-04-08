package com.user.management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {
    private String id;
    private String name;
    private String password;
    private String email;
}
