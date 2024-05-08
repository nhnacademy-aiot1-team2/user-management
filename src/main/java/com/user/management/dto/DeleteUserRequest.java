package com.user.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * The type Delete user request.
 *
 * @author parksangwon
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteUserRequest {
    @NotBlank
    private String id;
}
