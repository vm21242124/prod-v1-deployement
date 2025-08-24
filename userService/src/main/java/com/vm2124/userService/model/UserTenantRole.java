package com.vm2124.userService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_tenant_roles", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "role_id", "tenant_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTenantRole {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "generated_id", nullable = false, unique = true, length = 10)
    private String generatedId;
    
    @Column(name = "user_id", nullable = false)
    private String userId; // Reference to user generated ID
    
    @Column(name = "role_id", nullable = false)
    private String roleId; // Reference to role generated ID
    
    @Column(name = "tenant_id", nullable = false)
    private String tenantId; // Reference to tenant generated ID
    
    @Column(name = "assigned_by")
    private String assignedBy;
    
    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;
    
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "is_primary")
    private Boolean isPrimary = false; // Primary role for the user in this tenant
    
    @Column(name = "assigned_reason")
    private String assignedReason;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (assignedAt == null) {
            assignedAt = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Helper methods
    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }
    
    public boolean isValid() {
        return Boolean.TRUE.equals(isActive) && !isExpired();
    }
    
    public boolean isPrimaryRole() {
        return Boolean.TRUE.equals(isPrimary);
    }
}
