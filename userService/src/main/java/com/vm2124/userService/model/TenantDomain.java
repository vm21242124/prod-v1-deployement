package com.vm2124.userService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tenant_domains")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantDomain {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "tenant_id", nullable = false)
    private String tenantId; // Reference to tenant UUID as string
    
    @Column(nullable = false, unique = true)
    private String domain;
    
    @Column(name = "domain_type")
    @Enumerated(EnumType.STRING)
    private DomainType domainType = DomainType.CUSTOM;
    
    @Column(name = "is_primary")
    private Boolean isPrimary = false;
    
    @Column(name = "is_verified")
    private Boolean isVerified = false;
    
    @Column(name = "verification_token")
    private String verificationToken;
    
    @Column(name = "ssl_certificate", columnDefinition = "TEXT")
    private String sslCertificate;
    
    @Column(name = "ssl_private_key", columnDefinition = "TEXT")
    private String sslPrivateKey;
    
    @Column(name = "ssl_expires_at")
    private LocalDateTime sslExpiresAt;
    
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
    
    public enum DomainType {
        PRIMARY, CUSTOM, SUBDOMAIN, ALIAS
    }
}
