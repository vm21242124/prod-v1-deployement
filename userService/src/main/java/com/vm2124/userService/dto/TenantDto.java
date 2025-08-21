package com.vm2124.userService.dto;

import com.vm2124.userService.model.Tenant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantDto {
    
    private UUID id;
    private String tenantCode;
    private String name;
    private String domain;
    private String description;
    private Tenant.TenantStatus status;
    private Tenant.SubscriptionPlan subscriptionPlan;
    private Integer maxUsers;
    private String customDomain;
    private String logoUrl;
    private String primaryColor;
    private String secondaryColor;
    private String timezone;
    private String locale;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<String> domains;
    private Set<String> features;
    
    // Statistics
    private Long userCount;
    private Long activeUserCount;
    private Boolean isAtUserLimit;
}
