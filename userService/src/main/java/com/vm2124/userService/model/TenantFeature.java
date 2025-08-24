package com.vm2124.userService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tenant_features")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantFeature {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "generated_id", nullable = false, unique = true, length = 10)
    private String generatedId;
    
    @Column(name = "tenant_id", nullable = false)
    private String tenantId; // Reference to tenant generated ID
    
    @Column(nullable = false)
    private String featureCode;
    
    @Column(nullable = false)
    private String featureName;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "is_enabled")
    private Boolean isEnabled = false;
    
    @Column(name = "config_value", columnDefinition = "TEXT")
    private String configValue;
    
    @Column(name = "feature_type")
    @Enumerated(EnumType.STRING)
    private FeatureType featureType = FeatureType.BOOLEAN;
    
    @Column(name = "is_system_feature")
    private Boolean isSystemFeature = false;
    
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
    
    public enum FeatureType {
        BOOLEAN, STRING, INTEGER, JSON, ENCRYPTED
    }
}
