package com.vm2124.userService.dto;

import com.vm2124.userService.model.Tenant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantOnboardingRequest {
    
    // Tenant details
    private String tenantCode;
    private String name;
    private String domain;
    private String description;
    private Tenant.SubscriptionPlan subscriptionPlan;
    private Integer maxUsers;
    private String customDomain;
    private String logoUrl;
    private String primaryColor;
    private String secondaryColor;
    private String timezone;
    private String locale;
    
    // Admin user details
    private String adminUsername;
    private String adminEmail;
    private String adminFirstName;
    private String adminLastName;
    private String adminPassword;
    private String adminPhoneNumber;
    private String adminDepartment;
    private String adminJobTitle;
    
    // Modules/Features to enable
    private Set<String> enabledModules;
    
    // Additional configuration
    private Set<TenantConfigurationRequest> configurations;
}
