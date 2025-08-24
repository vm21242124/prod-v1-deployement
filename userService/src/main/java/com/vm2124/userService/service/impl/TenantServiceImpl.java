package com.vm2124.userService.service.impl;

import com.vm2124.userService.dto.TenantOnboardingRequest;
import com.vm2124.userService.dto.TenantOnboardingResponse;
import com.vm2124.userService.model.*;
import com.vm2124.userService.repository.*;
import com.vm2124.userService.service.IdsGeneraterService;
import com.vm2124.userService.service.TenantService;
import com.vm2124.userService.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TenantServiceImpl implements TenantService {
    
    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final TenantFeatureRepository tenantFeatureRepository;
    private final UserTenantRoleRepository userTenantRoleRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final IdsGeneraterService idsGeneraterService;
    
    @Override
    @Transactional
    public TenantOnboardingResponse onboardTenant(TenantOnboardingRequest request) {
        log.info("Starting tenant onboarding for tenant code: {}", request.getTenantCode());
        
        try {
            // 1. Create tenant
            Tenant tenant = createTenant(request);
            
            // 2. Enable modules/features
            Set<String> enabledFeatures = enableModules(tenant.getGeneratedId(), request.getEnabledModules());
            
            // 3. Create admin user
            User adminUser = createAdminUser(tenant, request);
            
            // 4. Assign admin role to user
            assignAdminRole(tenant.getGeneratedId(), adminUser.getGeneratedId());
            
            // 5. Create response
            TenantOnboardingResponse response = new TenantOnboardingResponse();
            response.setTenantId(tenant.getId());
            response.setTenantGeneratedId(tenant.getGeneratedId());
            response.setTenantCode(tenant.getTenantCode());
            response.setTenantName(tenant.getName());
            response.setDomain(tenant.getDomain());
            response.setStatus(tenant.getStatus().name());
            response.setCreatedAt(tenant.getCreatedAt());
            
            response.setAdminUserId(adminUser.getId());
            response.setAdminGeneratedId(adminUser.getGeneratedId());
            response.setAdminUsername(adminUser.getUsername());
            response.setAdminEmail(adminUser.getEmail());
            response.setAdminFullName(adminUser.getFirstName() + " " + adminUser.getLastName());
            
            response.setEnabledModules(request.getEnabledModules());
            response.setEnabledFeatures(enabledFeatures);
            
            response.setSuccess(true);
            response.setMessage("Tenant onboarded successfully");
            
            log.info("Tenant onboarding completed successfully for tenant: {}", tenant.getTenantCode());
            return response;
            
        } catch (Exception e) {
            log.error("Error during tenant onboarding: {}", e.getMessage(), e);
            TenantOnboardingResponse errorResponse = new TenantOnboardingResponse();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("Failed to onboard tenant: " + e.getMessage());
            return errorResponse;
        }
    }
    
    private Tenant createTenant(TenantOnboardingRequest request) {
        // Validate tenant code and domain uniqueness
        if (tenantRepository.existsByTenantCode(request.getTenantCode())) {
            throw new RuntimeException("Tenant code already exists: " + request.getTenantCode());
        }
        
        if (tenantRepository.existsByDomain(request.getDomain())) {
            throw new RuntimeException("Domain already exists: " + request.getDomain());
        }
        
        Tenant tenant = new Tenant();
        tenant.setGeneratedId(idsGeneraterService.generateTenantId());
        tenant.setTenantCode(request.getTenantCode());
        tenant.setName(request.getName());
        tenant.setDomain(request.getDomain());
        tenant.setDescription(request.getDescription());
        tenant.setSubscriptionPlan(request.getSubscriptionPlan() != null ? 
            request.getSubscriptionPlan() : Tenant.SubscriptionPlan.BASIC);
        tenant.setMaxUsers(request.getMaxUsers() != null ? request.getMaxUsers() : 100);
        tenant.setCustomDomain(request.getCustomDomain());
        tenant.setLogoUrl(request.getLogoUrl());
        tenant.setPrimaryColor(request.getPrimaryColor());
        tenant.setSecondaryColor(request.getSecondaryColor());
        tenant.setTimezone(request.getTimezone() != null ? request.getTimezone() : "UTC");
        tenant.setLocale(request.getLocale() != null ? request.getLocale() : "en_US");
        tenant.setStatus(Tenant.TenantStatus.ACTIVE);
        
        return tenantRepository.save(tenant);
    }
    
    private Set<String> enableModules(String tenantId, Set<String> moduleCodes) {
        Set<String> enabledFeatures = new HashSet<>();
        
        if (moduleCodes == null || moduleCodes.isEmpty()) {
            // Enable default modules
            moduleCodes = Set.of("USER_MANAGEMENT", "ROLE_MANAGEMENT", "TENANT_CONFIGURATION");
        }
        
        for (String moduleCode : moduleCodes) {
            TenantFeature feature = new TenantFeature();
            feature.setGeneratedId(idsGeneraterService.generateTenantFeatureId());
            feature.setTenantId(tenantId);
            feature.setFeatureCode(moduleCode);
            feature.setFeatureName(getModuleDisplayName(moduleCode));
            feature.setDescription("Module: " + moduleCode);
            feature.setIsEnabled(true);
            feature.setFeatureType(TenantFeature.FeatureType.BOOLEAN);
            feature.setIsSystemFeature(true);
            
            tenantFeatureRepository.save(feature);
            enabledFeatures.add(moduleCode);
        }
        
        return enabledFeatures;
    }
    
    private String getModuleDisplayName(String moduleCode) {
        return switch (moduleCode.toUpperCase()) {
            case "USER_MANAGEMENT" -> "User Management";
            case "ROLE_MANAGEMENT" -> "Role Management";
            case "TENANT_CONFIGURATION" -> "Tenant Configuration";
            case "AUDIT_LOGS" -> "Audit Logs";
            case "REPORTING" -> "Reporting";
            case "ANALYTICS" -> "Analytics";
            case "NOTIFICATIONS" -> "Notifications";
            case "INTEGRATIONS" -> "Integrations";
            default -> moduleCode;
        };
    }
    
    private User createAdminUser(Tenant tenant, TenantOnboardingRequest request) {
        // Validate admin user details
        if (userRepository.existsByUsernameAndTenantId(request.getAdminUsername(), tenant.getId().toString())) {
            throw new RuntimeException("Username already exists in tenant: " + request.getAdminUsername());
        }
        
        if (userRepository.existsByEmailAndTenantId(request.getAdminEmail(), tenant.getId().toString())) {
            throw new RuntimeException("Email already exists in tenant: " + request.getAdminEmail());
        }
        
        User adminUser = new User();
        adminUser.setGeneratedId(idsGeneraterService.generateUserId());
        adminUser.setTenantId(tenant.getGeneratedId());
        adminUser.setUsername(request.getAdminUsername());
        adminUser.setEmail(request.getAdminEmail());
        adminUser.setFirstName(request.getAdminFirstName());
        adminUser.setLastName(request.getAdminLastName());
        adminUser.setPassword(passwordEncoder.encode(request.getAdminPassword()));
        adminUser.setPhoneNumber(request.getAdminPhoneNumber());
        adminUser.setDepartment(request.getAdminDepartment());
        adminUser.setJobTitle(request.getAdminJobTitle());
        adminUser.setStatus(User.UserStatus.ACTIVE);
        adminUser.setPasswordChangedAt(LocalDateTime.now());
        
        return userRepository.save(adminUser);
    }
    
    private void assignAdminRole(String tenantId, String userId) {
        // Find or create TENANT_ADMIN role
        Roles adminRole = rolesRepository.findByRoleCodeAndTenantId("TENANT_ADMIN", tenantId)
            .orElseGet(() -> createTenantAdminRole(tenantId));
        
        // Create user-tenant-role relationship
        UserTenantRole userTenantRole = new UserTenantRole();
        userTenantRole.setGeneratedId(idsGeneraterService.generateUserTenantRoleId());
        userTenantRole.setUserId(userId);
        userTenantRole.setRoleId(adminRole.getGeneratedId());
        userTenantRole.setTenantId(tenantId);
        userTenantRole.setIsPrimary(true);
        userTenantRole.setIsActive(true);
        userTenantRole.setAssignedReason("Initial tenant setup");
        
        userTenantRoleRepository.save(userTenantRole);
    }
    
    private Roles createTenantAdminRole(String tenantId) {
        Roles adminRole = new Roles();
        adminRole.setGeneratedId(idsGeneraterService.generateRoleId());
        adminRole.setTenantId(tenantId);
        adminRole.setRoleCode("TENANT_ADMIN");
        adminRole.setRoleName("Tenant Administrator");
        adminRole.setDescription("Administrator role for tenant management");
        adminRole.setIsSystemRole(false);
        adminRole.setIsDefault(false);
        adminRole.setIsActive(true);
        adminRole.setRoleType(Roles.RoleType.TENANT_ADMIN);
        adminRole.setPriority(100);
        
        // Add admin permissions
        Set<String> adminPermissions = new HashSet<>();
        adminPermissions.add("USER_CREATE");
        adminPermissions.add("USER_READ");
        adminPermissions.add("USER_UPDATE");
        adminPermissions.add("USER_DELETE");
        adminPermissions.add("ROLE_CREATE");
        adminPermissions.add("ROLE_READ");
        adminPermissions.add("ROLE_UPDATE");
        adminPermissions.add("ROLE_DELETE");
        adminPermissions.add("TENANT_CONFIG_READ");
        adminPermissions.add("TENANT_CONFIG_UPDATE");
        adminRole.setPermissionCodes(adminPermissions);
        
        return rolesRepository.save(adminRole);
    }
}
