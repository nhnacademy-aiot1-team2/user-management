package com.user.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type User update request.
 *
 * @author parksangwon
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    private String name;
    private String password;
    private String email;
}
