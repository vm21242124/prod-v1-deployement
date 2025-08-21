package com.vm2124.userService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignRoleRequest {
    
    private String userId;
    private Set<String> roleIds;
    private Boolean isPrimary;
    private LocalDateTime expiresAt;
    private String assignedReason;
    private String assignedBy;
}
