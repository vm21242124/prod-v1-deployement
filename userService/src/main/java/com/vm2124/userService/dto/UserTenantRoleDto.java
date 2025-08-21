package com.vm2124.userService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTenantRoleDto {
    
    private UUID id;
    private String userId;
    private String username;
    private String userEmail;
    private String userName;
    private String roleId;
    private String roleCode;
    private String roleName;
    private String tenantId;
    private String tenantCode;
    private String tenantName;
    private String assignedBy;
    private LocalDateTime assignedAt;
    private LocalDateTime expiresAt;
    private Boolean isActive;
    private Boolean isPrimary;
    private String assignedReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Computed fields
    private Boolean isExpired;
    private Boolean isValid;
    private String status;
    private Long daysUntilExpiry;
}
