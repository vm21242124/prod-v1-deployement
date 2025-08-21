package com.vm2124.userService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantRoleDto {
    
    private Long id;
    private Long tenantId;
    private String tenantCode;
    private String name;
    private String description;
    private Boolean isSystemRole;
    private Boolean isDefault;
    private Set<String> permissions;
    private Long userCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
