package com.vm2124.userService.dto;

import com.vm2124.userService.model.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoleRequest {
    
    private String roleCode;
    private String roleName;
    private String description;
    private Boolean isDefault;
    private Boolean isActive;
    private Roles.RoleType roleType;
    private Integer priority;
    private Set<String> permissionCodes;
    private String assignedReason;
}
