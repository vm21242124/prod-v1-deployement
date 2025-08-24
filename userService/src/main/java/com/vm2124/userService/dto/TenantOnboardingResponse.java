package com.vm2124.userService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantOnboardingResponse {
    
    private UUID tenantId;
    private String tenantGeneratedId;
    private String tenantCode;
    private String tenantName;
    private String domain;
    private String status;
    private LocalDateTime createdAt;
    
    private UUID adminUserId;
    private String adminGeneratedId;
    private String adminUsername;
    private String adminEmail;
    private String adminFullName;
    
    private Set<String> enabledModules;
    private Set<String> enabledFeatures;
    
    private String message;
    private boolean success;
}
