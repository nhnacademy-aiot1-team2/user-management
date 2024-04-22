package com.user.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Role response.
 *
 * @author jongsikk
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponse {
    Long roleId;
    String roleName;
}
