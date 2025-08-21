package com.vm2124.userService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDto {
    
    private UUID id;
    private String permissionCode;
    private String permissionName;
    private String description;
    private String resourceType;
    private String actionType;
    private Boolean isSystemPermission;
    private Boolean isActive;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Computed fields
    private String displayName;
    private Boolean isAssigned;
}
