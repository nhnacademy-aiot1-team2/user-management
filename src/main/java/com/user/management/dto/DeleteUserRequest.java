package com.user.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String id;
}
