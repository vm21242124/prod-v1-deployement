package com.vm2124.userService.dto;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;

@Data
@Component
@RequestScope
public class UserContext {
    private String userId;
    private String userGeneratedId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private String tenantId;
    private String tenantGeneratedId;
    private List<String> roles;
    private List<String> permissions;
    
    public boolean isAuthenticated() {
        return userId != null && !userId.isEmpty();
    }
    
    public boolean hasRole(String roleCode) {
        return roles != null && roles.contains(roleCode);
    }
    
    public boolean hasPermission(String permission) {
        return permissions != null && permissions.contains(permission);
    }
    
    public boolean hasAnyRole(List<String> roleCodes) {
        return roles != null && roles.stream().anyMatch(roleCodes::contains);
    }
    
    public boolean hasAnyPermission(List<String> permissions) {
        return this.permissions != null && this.permissions.stream().anyMatch(permissions::contains);
    }
}
