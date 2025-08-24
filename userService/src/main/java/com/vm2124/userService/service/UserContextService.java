package com.vm2124.userService.service;

import com.vm2124.userService.dto.UserContext;
import com.vm2124.userService.dto.UserInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserContextService {

    private final UserContext userContext;

    /**
     * Get the current user's ID
     */
    public String getCurrentUserId() {
        return userContext.getUserId();
    }

    /**
     * Get the current user's generated ID
     */
    public String getCurrentUserGeneratedId() {
        return userContext.getUserGeneratedId();
    }

    /**
     * Get the current user's username
     */
    public String getCurrentUsername() {
        return userContext.getUsername();
    }

    /**
     * Get the current user's email
     */
    public String getCurrentUserEmail() {
        return userContext.getEmail();
    }

    /**
     * Get the current user's full name
     */
    public String getCurrentUserFullName() {
        if (userContext.getFirstName() != null && userContext.getLastName() != null) {
            return userContext.getFirstName() + " " + userContext.getLastName();
        }
        return userContext.getUsername();
    }

    /**
     * Get the current tenant ID
     */
    public String getCurrentTenantId() {
        return userContext.getTenantId();
    }

    /**
     * Get the current tenant's generated ID
     */
    public String getCurrentTenantGeneratedId() {
        return userContext.getTenantGeneratedId();
    }

    /**
     * Check if the current user is authenticated
     */
    public boolean isAuthenticated() {
        return userContext.isAuthenticated();
    }

    /**
     * Check if the current user is active
     */
    public boolean isActive() {
        return userContext.isActive();
    }

    /**
     * Get the current user's roles
     */
    public List<String> getCurrentUserRoles() {
        return userContext.getRoles();
    }

    /**
     * Get the current user's permissions
     */
    public List<String> getCurrentUserPermissions() {
        return userContext.getPermissions();
    }

    /**
     * Check if the current user has a specific role
     */
    public boolean hasRole(String roleCode) {
        return userContext.hasRole(roleCode);
    }

    /**
     * Check if the current user has any of the specified roles
     */
    public boolean hasAnyRole(List<String> roleCodes) {
        return userContext.hasAnyRole(roleCodes);
    }

    /**
     * Check if the current user has a specific permission
     */
    public boolean hasPermission(String permission) {
        return userContext.hasPermission(permission);
    }

    /**
     * Check if the current user has any of the specified permissions
     */
    public boolean hasAnyPermission(List<String> permissions) {
        return userContext.hasAnyPermission(permissions);
    }

    /**
     * Get the complete user context
     */
    public UserContext getUserContext() {
        return userContext;
    }

    /**
     * Convert the current user context to UserInfoDto
     */
    public UserInfoDto toUserInfoDto() {
        if (!isAuthenticated()) {
            return new UserInfoDto(false, "User not authenticated", null, null, null, null);
        }

        // Build user data
        UserInfoDto.UserData userData = new UserInfoDto.UserData();
        userData.setId(userContext.getUserId());
        userData.setGeneratedId(userContext.getUserGeneratedId());
        userData.setUsername(userContext.getUsername());
        userData.setEmail(userContext.getEmail());
        userData.setFirstName(userContext.getFirstName());
        userData.setLastName(userContext.getLastName());
        userData.setActive(userContext.isActive());
        userData.setTenantId(userContext.getTenantId());
        userData.setTenantGeneratedId(userContext.getTenantGeneratedId());
        userData.setCreatedAt(LocalDateTime.now()); // This would come from the user entity if needed
        userData.setLastLoginAt(LocalDateTime.now()); // This would come from the user entity if needed

        return new UserInfoDto(
            true,
            "User information retrieved from context",
            userData,
            null, // Tenant data would need to be fetched separately if needed
            null, // Role data would need to be fetched separately if needed
            userContext.getPermissions()
        );
    }

    /**
     * Require authentication - throws exception if user is not authenticated
     */
    public void requireAuthentication() {
        if (!isAuthenticated()) {
            throw new SecurityException("Authentication required");
        }
    }

    /**
     * Require a specific role - throws exception if user doesn't have the role
     */
    public void requireRole(String roleCode) {
        requireAuthentication();
        if (!hasRole(roleCode)) {
            throw new SecurityException("Role '" + roleCode + "' required");
        }
    }

    /**
     * Require a specific permission - throws exception if user doesn't have the permission
     */
    public void requirePermission(String permission) {
        requireAuthentication();
        if (!hasPermission(permission)) {
            throw new SecurityException("Permission '" + permission + "' required");
        }
    }
}
