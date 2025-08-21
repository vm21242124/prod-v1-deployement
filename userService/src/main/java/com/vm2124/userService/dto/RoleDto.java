package com.vm2124.userService.dto;

import com.vm2124.userService.model.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
    
    private UUID id;
    private String tenantId;
    private String tenantCode;
    private String roleCode;
    private String roleName;
    private String description;
    private Boolean isSystemRole;
    private Boolean isDefault;
    private Boolean isActive;
    private Roles.RoleType roleType;
    private Integer priority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Related data
    private Set<String> permissions;
    private Long userCount;
    private Boolean canEdit;
    private Boolean canDelete;
    
    // Computed fields
    private String displayName;
    private String category;
}
