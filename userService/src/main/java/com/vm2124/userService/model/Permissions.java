package com.vm2124.userService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "permissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permissions {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false, unique = true)
    private String permissionCode;
    
    @Column(nullable = false)
    private String permissionName;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "resource_type")
    private String resourceType; // e.g., "USER", "TENANT", "ROLE", "CONFIGURATION"
    
    @Column(name = "action_type")
    private String actionType; // e.g., "CREATE", "READ", "UPDATE", "DELETE", "MANAGE"
    
    @Column(name = "is_system_permission")
    private Boolean isSystemPermission = true;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "category")
    private String category; // e.g., "USER_MANAGEMENT", "TENANT_MANAGEMENT", "SECURITY"
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Predefined permission constants
    public static final class PermissionCodes {
        // User Management
        public static final String USER_CREATE = "USER_CREATE";
        public static final String USER_READ = "USER_READ";
        public static final String USER_UPDATE = "USER_UPDATE";
        public static final String USER_DELETE = "USER_DELETE";
        public static final String USER_MANAGE = "USER_MANAGE";
        
        // Role Management
        public static final String ROLE_CREATE = "ROLE_CREATE";
        public static final String ROLE_READ = "ROLE_READ";
        public static final String ROLE_UPDATE = "ROLE_UPDATE";
        public static final String ROLE_DELETE = "ROLE_DELETE";
        
        public static final String ROLE_ASSIGN = "ROLE_ASSIGN";
        
        // Tenant Management
        public static final String TENANT_READ = "TENANT_READ";
        public static final String TENANT_UPDATE = "TENANT_UPDATE";
        public static final String TENANT_MANAGE = "TENANT_MANAGE";
        public static final String TENANT_CONFIGURATION = "TENANT_CONFIGURATION";
        
        // Domain Management
        public static final String DOMAIN_CREATE = "DOMAIN_CREATE";
        public static final String DOMAIN_READ = "DOMAIN_READ";
        public static final String DOMAIN_UPDATE = "DOMAIN_UPDATE";
        public static final String DOMAIN_DELETE = "DOMAIN_DELETE";
        
        // Feature Management
        public static final String FEATURE_ENABLE = "FEATURE_ENABLE";
        public static final String FEATURE_DISABLE = "FEATURE_DISABLE";
        public static final String FEATURE_READ = "FEATURE_READ";
        
        // Invitation Management
        public static final String INVITATION_CREATE = "INVITATION_CREATE";
        public static final String INVITATION_READ = "INVITATION_READ";
        public static final String INVITATION_CANCEL = "INVITATION_CANCEL";
        
        // Audit and Reports
        public static final String AUDIT_READ = "AUDIT_READ";
        public static final String REPORT_GENERATE = "REPORT_GENERATE";
        
        // System Administration
        public static final String SYSTEM_ADMIN = "SYSTEM_ADMIN";
        public static final String TENANT_ADMIN = "TENANT_ADMIN";
    }
    
    public static final class Categories {
        public static final String USER_MANAGEMENT = "USER_MANAGEMENT";
        public static final String ROLE_MANAGEMENT = "ROLE_MANAGEMENT";
        public static final String TENANT_MANAGEMENT = "TENANT_MANAGEMENT";
        public static final String DOMAIN_MANAGEMENT = "DOMAIN_MANAGEMENT";
        public static final String FEATURE_MANAGEMENT = "FEATURE_MANAGEMENT";
        public static final String INVITATION_MANAGEMENT = "INVITATION_MANAGEMENT";
        public static final String AUDIT_REPORTS = "AUDIT_REPORTS";
        public static final String SYSTEM_ADMINISTRATION = "SYSTEM_ADMINISTRATION";
    }
}
