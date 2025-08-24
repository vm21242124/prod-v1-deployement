package com.vm2124.userService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
    private boolean success;
    private String message;
    private UserData user;
    private TenantData tenant;
    private List<RoleData> roles;
    private List<String> permissions;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserData {
        private String id;
        private String generatedId;
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private boolean isActive;
        private String tenantId;
        private String tenantGeneratedId;
        private LocalDateTime createdAt;
        private LocalDateTime lastLoginAt;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TenantData {
        private String id;
        private String generatedId;
        private String name;
        private String domain;
        private String status;
        private String plan;
        private LocalDateTime createdAt;
        private List<String> enabledModules;
        private Map<String, Object> configuration;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoleData {
        private String id;
        private String generatedId;
        private String roleCode;
        private String roleName;
        private String description;
        private int priority;
        private String roleType;
        private boolean isSystemRole;
        private boolean isDefault;
        private Set<String> permissions;
    }
}
