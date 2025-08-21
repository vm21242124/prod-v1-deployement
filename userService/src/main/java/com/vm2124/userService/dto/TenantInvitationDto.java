package com.vm2124.userService.dto;

import com.vm2124.userService.model.TenantInvitation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantInvitationDto {
    
    private UUID id;
    private String tenantId;
    private String tenantCode;
    private String tenantName;
    private String email;
    private String firstName;
    private String lastName;
    private String invitationToken;
    private String invitedById;
    private String invitedByName;
    private String roleId;
    private String roleName;
    private String department;
    private String jobTitle;
    private TenantInvitation.InvitationStatus status;
    private LocalDateTime expiresAt;
    private LocalDateTime acceptedAt;
    private String acceptedById;
    private String acceptedByName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Computed fields
    private Boolean isExpired;
    private String invitationUrl;
}
