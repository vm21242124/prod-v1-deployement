package com.vm2124.userService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Roles {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "tenant_id")
    private String tenantId; // Reference to tenant UUID as string (null for system-wide roles)
    
    @Column(nullable = false)
    private String roleCode;
    
    @Column(nullable = false)
    private String roleName;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "is_system_role")
    private Boolean isSystemRole = false;
    
    @Column(name = "is_default")
    private Boolean isDefault = false;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "role_type")
    @Enumerated(EnumType.STRING)
    private RoleType roleType = RoleType.CUSTOM;
    
    @Column(name = "priority")
    private Integer priority = 0; // Higher number = higher priority
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "role_permissions", 
                     joinColumns = @JoinColumn(name = "role_id"))
    @Column(name = "permission_code")
    private Set<String> permissionCodes = new HashSet<>();
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum RoleType {
        SYSTEM, // System-wide predefined roles
        TENANT_ADMIN, // Tenant administrator role
        TENANT_USER, // Regular tenant user role
        CUSTOM // Tenant-custom roles
    }
    
    // Predefined system role constants
    public static final class SystemRoles {
        public static final String SUPER_ADMIN = "SUPER_ADMIN";
        public static final String SYSTEM_ADMIN = "SYSTEM_ADMIN";
        public static final String TENANT_ADMIN = "TENANT_ADMIN";
        public static final String TENANT_USER = "TENANT_USER";
        public static final String GUEST = "GUEST";
    }
    
    // Helper methods
    public boolean hasPermission(String permissionCode) {
        return permissionCodes.contains(permissionCode);
    }
    
    public boolean isSystemRole() {
        return Boolean.TRUE.equals(isSystemRole);
    }
    
    public boolean isTenantRole() {
        return tenantId != null;
    }
    
    public void addPermission(String permissionCode) {
        this.permissionCodes.add(permissionCode);
    }
    
    public void removePermission(String permissionCode) {
        this.permissionCodes.remove(permissionCode);
    }
}
