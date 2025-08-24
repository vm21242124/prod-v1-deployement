package com.vm2124.userService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnboardDataResponse {
    
    private boolean success;
    private String message;
    private LocalDateTime timestamp;
    
    // Count of created items
    private int modulesCreated;
    private int permissionsCreated;
    private int rolesCreated;
    private int featuresCreated;
    
    // Details of created items
    private Set<String> createdModules;
    private Set<String> createdPermissions;
    private Set<String> createdRoles;
    private Set<String> createdFeatures;
    
    // Status information
    private Map<String, Object> status;
    
    // Error details if any
    private String errorDetails;
    
    public OnboardDataResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
