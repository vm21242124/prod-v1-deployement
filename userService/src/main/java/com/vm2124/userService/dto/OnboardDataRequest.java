package com.vm2124.userService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnboardDataRequest {
    
    // Specify which data types to onboard
    private boolean onboardModules = true;
    private boolean onboardPermissions = true;
    private boolean onboardRoles = true;
    private boolean onboardFeatures = true;
    
    // Specific modules to onboard (if empty, onboard all predefined modules)
    private Set<String> specificModules;
    
    // Specific permissions to onboard (if empty, onboard all predefined permissions)
    private Set<String> specificPermissions;
    
    // Specific roles to onboard (if empty, onboard all predefined roles)
    private Set<String> specificRoles;
    
    // Specific features to onboard (if empty, onboard all predefined features)
    private Set<String> specificFeatures;
    
    // Force recreation of existing data
    private boolean forceRecreate = false;
    
    // Tenant ID if onboarding tenant-specific data
    private String tenantId;
}
