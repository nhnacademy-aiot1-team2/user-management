package com.user.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

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
    @NotBlank
    private String name;
    @NotBlank
    private String password;
    @NotBlank
    private String email;
}
