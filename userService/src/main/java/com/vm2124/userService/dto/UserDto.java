package com.vm2124.userService.dto;

import com.vm2124.userService.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    
    private UUID id;
    private String tenantId;
    private String tenantCode;
    private String tenantName;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private User.UserStatus status;
    private String employeeId;
    private String phoneNumber;
    private String department;
    private String jobTitle;
    private String managerId;
    private String managerName;
    private LocalDateTime lastLoginAt;
    private LocalDateTime passwordChangedAt;
    private Integer failedLoginAttempts;
    private LocalDateTime accountLockedUntil;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<String> roles;
    private Set<String> permissions;
    
    // Additional fields for multi-tenant context
    private Boolean isTenantAdmin;
    private Boolean isSystemAdmin;
    private String tenantDomain;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class CreateUserRequest {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String tenantId;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class UpdateUserRequest {
    private String email;
    private String firstName;
    private String lastName;
    private String status;
    private String tenantId;
    private String tenantCode;
    private String tenantName;
    private String employeeId;
    private String phoneNumber;
    private String department;
    private String jobTitle;
    private String managerId;
    private String managerName;
    private LocalDateTime lastLoginAt;
    private LocalDateTime passwordChangedAt;
    private Integer failedLoginAttempts;
    private LocalDateTime accountLockedUntil;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Role and permission information
    private Set<String> roles;
    private Set<String> permissions;
    private String primaryRole;
    private Boolean isTenantAdmin;
    private Boolean isSystemAdmin;
    private String tenantDomain;
}
