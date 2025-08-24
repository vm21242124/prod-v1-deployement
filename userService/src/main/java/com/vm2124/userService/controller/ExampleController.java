package com.vm2124.userService.controller;

import com.vm2124.userService.service.UserContextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/example")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ExampleController {

    private final UserContextService userContextService;

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfile() {
        log.info("Getting user profile using context");
        
        try {
            // Require authentication
            userContextService.requireAuthentication();
            
            Map<String, Object> profile = new HashMap<>();
            profile.put("userId", userContextService.getCurrentUserId());
            profile.put("username", userContextService.getCurrentUsername());
            profile.put("email", userContextService.getCurrentUserEmail());
            profile.put("fullName", userContextService.getCurrentUserFullName());
            profile.put("tenantId", userContextService.getCurrentTenantId());
            profile.put("roles", userContextService.getCurrentUserRoles());
            profile.put("permissions", userContextService.getCurrentUserPermissions());
            
            return ResponseEntity.ok(profile);
            
        } catch (SecurityException e) {
            log.warn("Authentication required: {}", e.getMessage());
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error getting profile: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }

    @GetMapping("/admin-only")
    public ResponseEntity<Map<String, Object>> adminOnlyEndpoint() {
        log.info("Accessing admin-only endpoint");
        
        try {
            // Require admin role
            userContextService.requireRole("ADMIN");
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Welcome to admin area!");
            response.put("user", userContextService.getCurrentUsername());
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
            
        } catch (SecurityException e) {
            log.warn("Access denied: {}", e.getMessage());
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error in admin endpoint: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }

    @GetMapping("/user-management")
    public ResponseEntity<Map<String, Object>> userManagementEndpoint() {
        log.info("Accessing user management endpoint");
        
        try {
            // Require specific permission
            userContextService.requirePermission("USER_MANAGEMENT");
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User management access granted");
            response.put("user", userContextService.getCurrentUsername());
            response.put("tenant", userContextService.getCurrentTenantId());
            
            return ResponseEntity.ok(response);
            
        } catch (SecurityException e) {
            log.warn("Access denied: {}", e.getMessage());
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error in user management endpoint: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }

    @GetMapping("/check-permissions")
    public ResponseEntity<Map<String, Object>> checkPermissions() {
        log.info("Checking user permissions");
        
        try {
            userContextService.requireAuthentication();
            
            Map<String, Object> response = new HashMap<>();
            response.put("authenticated", userContextService.isAuthenticated());
            response.put("active", userContextService.isActive());
            response.put("hasAdminRole", userContextService.hasRole("ADMIN"));
            response.put("hasUserManagementPermission", userContextService.hasPermission("USER_MANAGEMENT"));
            response.put("allRoles", userContextService.getCurrentUserRoles());
            response.put("allPermissions", userContextService.getCurrentUserPermissions());
            
            return ResponseEntity.ok(response);
            
        } catch (SecurityException e) {
            log.warn("Authentication required: {}", e.getMessage());
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error checking permissions: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }
}
