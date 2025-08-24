package com.vm2124.userService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tenants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tenant {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "generated_id", nullable = false, unique = true, length = 10)
    private String generatedId;
    
    @Column(nullable = false, unique = true)
    private String tenantCode;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String domain;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    private TenantStatus status = TenantStatus.ACTIVE;
    
    @Column(name = "subscription_plan")
    @Enumerated(EnumType.STRING)
    private SubscriptionPlan subscriptionPlan = SubscriptionPlan.BASIC;
    
    @Column(name = "max_users")
    private Integer maxUsers = 100;
    
    @Column(name = "custom_domain")
    private String customDomain;
    
    @Column(name = "logo_url")
    private String logoUrl;
    
    @Column(name = "primary_color")
    private String primaryColor;
    
    @Column(name = "secondary_color")
    private String secondaryColor;
    
    @Column(name = "timezone")
    private String timezone = "UTC";
    
    @Column(name = "locale")
    private String locale = "en_US";
    
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
    
    public enum TenantStatus {
        ACTIVE, INACTIVE, SUSPENDED, PENDING_ACTIVATION
    }
    
    public enum SubscriptionPlan {
        BASIC, PREMIUM, ENTERPRISE, CUSTOM
    }
}
