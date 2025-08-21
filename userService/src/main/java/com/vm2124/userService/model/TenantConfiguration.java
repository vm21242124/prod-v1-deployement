package com.vm2124.userService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tenant_configurations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantConfiguration {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "tenant_id", nullable = false)
    private String tenantId; // Reference to tenant UUID as string
    
    @Column(nullable = false)
    private String configKey;
    
    @Column(columnDefinition = "TEXT")
    private String configValue;
    
    @Column(name = "config_type")
    @Enumerated(EnumType.STRING)
    private ConfigType configType = ConfigType.STRING;
    
    @Column(name = "is_encrypted")
    private Boolean isEncrypted = false;
    
    @Column(name = "is_required")
    private Boolean isRequired = false;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
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
    
    public enum ConfigType {
        STRING, INTEGER, BOOLEAN, JSON, ENCRYPTED
    }
}
