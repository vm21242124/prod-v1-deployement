package com.vm2124.userService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tenant_invitations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantInvitation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "tenant_id", nullable = false)
    private String tenantId; // Reference to tenant UUID as string
    
    @Column(nullable = false)
    private String email;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    @Column(name = "invitation_token", nullable = false, unique = true)
    private String invitationToken;
    
    @Column(name = "invited_by")
    private String invitedById; // Reference to user UUID as string
    
    @Column(name = "role_id")
    private String roleId; // Reference to role UUID as string
    
    @Column(name = "department")
    private String department;
    
    @Column(name = "job_title")
    private String jobTitle;
    
    @Enumerated(EnumType.STRING)
    private InvitationStatus status = InvitationStatus.PENDING;
    
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
    
    @Column(name = "accepted_at")
    private LocalDateTime acceptedAt;
    
    @Column(name = "accepted_by")
    private String acceptedById; // Reference to user UUID as string
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (expiresAt == null) {
            expiresAt = LocalDateTime.now().plusDays(7); // Default 7 days expiry
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum InvitationStatus {
        PENDING, ACCEPTED, EXPIRED, CANCELLED
    }
}
