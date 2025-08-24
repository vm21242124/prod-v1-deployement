package com.vm2124.userService.service;

import com.vm2124.userService.dto.UserInfoDto;
import com.vm2124.userService.model.*;
import com.vm2124.userService.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenValidationService {

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final UserTenantRoleRepository userTenantRoleRepository;
    private final RolesRepository rolesRepository;
    private final TenantFeatureRepository tenantFeatureRepository;
    private final JwtService jwtService;

    public UserInfoDto validateTokenAndGetUserInfo(String token) {
        log.info("Validating token and fetching comprehensive user information");
        
        try {
            // Validate the token using JWT service
            if (!jwtService.validateToken(token)) {
                return new UserInfoDto(false, "Invalid or expired token", null, null, null, null);
            }
            
            // Extract user ID from token
            String userId = jwtService.extractUserId(token);
            
            if (userId == null) {
                return new UserInfoDto(false, "Invalid token format", null, null, null, null);
            }
            
            // Find user by ID
            Optional<User> userOpt = userRepository.findById(UUID.fromString(userId));
            if (userOpt.isEmpty()) {
                return new UserInfoDto(false, "User not found", null, null, null, null);
            }

            User user = userOpt.get();
            
            // Build user data
            UserInfoDto.UserData userData = buildUserData(user);
            
            // Build tenant data
            UserInfoDto.TenantData tenantData = buildTenantData(user.getTenantId());
            
            // Build roles and permissions data
            List<UserInfoDto.RoleData> rolesData = buildRolesData(user.getId().toString());
            List<String> permissionsList = buildPermissionsList(user.getId().toString());
            
            return new UserInfoDto(
                true,
                "User information retrieved successfully",
                userData,
                tenantData,
                rolesData,
                permissionsList
            );
            
        } catch (Exception e) {
            log.error("Error validating token: {}", e.getMessage(), e);
            return new UserInfoDto(false, "Error validating token: " + e.getMessage(), null, null, null, null);
        }
    }

    private UserInfoDto.UserData buildUserData(User user) {
        UserInfoDto.UserData userData = new UserInfoDto.UserData();
        userData.setId(user.getId().toString());
        userData.setGeneratedId(user.getGeneratedId());
        userData.setUsername(user.getUsername());
        userData.setEmail(user.getEmail());
        userData.setFirstName(user.getFirstName());
        userData.setLastName(user.getLastName());
        userData.setActive(user.getStatus() == User.UserStatus.ACTIVE);
        userData.setTenantId(user.getTenantId());
        userData.setTenantGeneratedId(user.getTenantId()); // Assuming tenantId is the generated ID
        userData.setCreatedAt(user.getCreatedAt());
        userData.setLastLoginAt(user.getLastLoginAt());
        return userData;
    }

    private UserInfoDto.TenantData buildTenantData(String tenantId) {
        if (tenantId == null) {
            return null;
        }
        
        Optional<Tenant> tenantOpt = tenantRepository.findByGeneratedId(tenantId);
        if (tenantOpt.isEmpty()) {
            return null;
        }
        
        Tenant tenant = tenantOpt.get();
        UserInfoDto.TenantData tenantData = new UserInfoDto.TenantData();
        tenantData.setId(tenant.getId().toString());
        tenantData.setGeneratedId(tenant.getGeneratedId());
        tenantData.setName(tenant.getName());
        tenantData.setDomain(tenant.getDomain());
        tenantData.setStatus(tenant.getStatus().toString());
        tenantData.setPlan(tenant.getSubscriptionPlan().toString());
        tenantData.setCreatedAt(tenant.getCreatedAt());
        
        // Get enabled modules/features for this tenant
        List<TenantFeature> tenantFeatures = tenantFeatureRepository.findByTenantId(tenant.getGeneratedId());
        List<String> enabledModules = tenantFeatures.stream()
            .filter(TenantFeature::getIsEnabled)
            .map(TenantFeature::getFeatureCode)
            .collect(Collectors.toList());
        tenantData.setEnabledModules(enabledModules);
        
        // Set configuration
        Map<String, Object> configuration = new HashMap<>();
        configuration.put("maxUsers", tenant.getMaxUsers());
        configuration.put("subscriptionPlan", tenant.getSubscriptionPlan().toString());
        configuration.put("isActive", tenant.getStatus() == Tenant.TenantStatus.ACTIVE);
        tenantData.setConfiguration(configuration);
        
        return tenantData;
    }

    private List<UserInfoDto.RoleData> buildRolesData(String userId) {
        List<UserInfoDto.RoleData> rolesData = new ArrayList<>();
        
        List<UserTenantRole> userRoles = userTenantRoleRepository.findByUserId(userId);
        for (UserTenantRole userRole : userRoles) {
            Optional<Roles> roleOpt = rolesRepository.findByGeneratedId(userRole.getRoleId());
            if (roleOpt.isPresent()) {
                Roles role = roleOpt.get();
                UserInfoDto.RoleData roleData = buildRoleData(role);
                rolesData.add(roleData);
            }
        }
        
        return rolesData;
    }

    private List<String> buildPermissionsList(String userId) {
        Set<String> allPermissions = new HashSet<>();
        
        List<UserTenantRole> userRoles = userTenantRoleRepository.findByUserId(userId);
        for (UserTenantRole userRole : userRoles) {
            Optional<Roles> roleOpt = rolesRepository.findByGeneratedId(userRole.getRoleId());
            if (roleOpt.isPresent()) {
                Roles role = roleOpt.get();
                // Add permissions from this role
                if (role.getPermissionCodes() != null) {
                    allPermissions.addAll(role.getPermissionCodes());
                }
            }
        }
        
        return new ArrayList<>(allPermissions);
    }

    private UserInfoDto.RoleData buildRoleData(Roles role) {
        UserInfoDto.RoleData roleData = new UserInfoDto.RoleData();
        roleData.setId(role.getId().toString());
        roleData.setGeneratedId(role.getGeneratedId());
        roleData.setRoleCode(role.getRoleCode());
        roleData.setRoleName(role.getRoleName());
        roleData.setDescription(role.getDescription());
        roleData.setPriority(role.getPriority());
        roleData.setRoleType(role.getRoleType().toString());
        roleData.setSystemRole(role.getIsSystemRole());
        roleData.setDefault(role.getIsDefault());
        roleData.setPermissions(role.getPermissionCodes());
        return roleData;
    }
}
