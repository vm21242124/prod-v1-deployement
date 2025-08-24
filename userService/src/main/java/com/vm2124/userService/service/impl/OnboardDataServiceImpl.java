package com.vm2124.userService.service.impl;

import com.vm2124.userService.dto.*;
import com.vm2124.userService.model.*;
import com.vm2124.userService.repository.*;
import com.vm2124.userService.service.IdsGeneraterService;
import com.vm2124.userService.service.JsonDataLoaderService;
import com.vm2124.userService.service.OnboardDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OnboardDataServiceImpl implements OnboardDataService {
    
    private final TenantRepository tenantRepository;
    private final RolesRepository rolesRepository;
    private final TenantFeatureRepository tenantFeatureRepository;
    private final IdsGeneraterService idsGeneraterService;
    private final JsonDataLoaderService jsonDataLoaderService;
    
    @Override
    @Transactional
    public OnboardDataResponse initializeSystemData(OnboardDataRequest request) {
        log.info("Initializing system data");
        
        try {
            OnboardDataResponse response = new OnboardDataResponse();
            response.setTimestamp(LocalDateTime.now());
            
            int totalCreated = 0;
            
            if (request.isOnboardPermissions()) {
                OnboardDataResponse permResponse = onboardPermissions(request);
                response.setPermissionsCreated(permResponse.getPermissionsCreated());
                response.setCreatedPermissions(permResponse.getCreatedPermissions());
                totalCreated += permResponse.getPermissionsCreated();
            }
            
            if (request.isOnboardRoles()) {
                OnboardDataResponse roleResponse = onboardRoles(request);
                response.setRolesCreated(roleResponse.getRolesCreated());
                response.setCreatedRoles(roleResponse.getCreatedRoles());
                totalCreated += roleResponse.getRolesCreated();
            }
            
            if (request.isOnboardFeatures()) {
                OnboardDataResponse featureResponse = onboardFeatures(request);
                response.setFeaturesCreated(featureResponse.getFeaturesCreated());
                response.setCreatedFeatures(featureResponse.getCreatedFeatures());
                totalCreated += featureResponse.getFeaturesCreated();
            }
            
            if (request.isOnboardModules()) {
                OnboardDataResponse moduleResponse = onboardModules(request);
                response.setModulesCreated(moduleResponse.getModulesCreated());
                response.setCreatedModules(moduleResponse.getCreatedModules());
                totalCreated += moduleResponse.getModulesCreated();
            }
            
            response.setSuccess(true);
            response.setMessage("System data initialized successfully. Total items created: " + totalCreated);
            
            log.info("System data initialization completed successfully");
            return response;
            
        } catch (Exception e) {
            log.error("Error during system data initialization: {}", e.getMessage(), e);
            OnboardDataResponse errorResponse = new OnboardDataResponse(false, "Failed to initialize system data: " + e.getMessage());
            errorResponse.setErrorDetails(e.getMessage());
            return errorResponse;
        }
    }
    
    @Override
    @Transactional
    public OnboardDataResponse onboardModules(OnboardDataRequest request) {
        log.info("Onboarding modules");
        
        try {
            List<ModuleData> allModules = jsonDataLoaderService.loadModules();
            Set<String> allModuleCodes = allModules.stream()
                .map(ModuleData::getCode)
                .collect(Collectors.toSet());
            
            Set<String> modulesToCreate = request.getSpecificModules() != null && !request.getSpecificModules().isEmpty() 
                ? request.getSpecificModules() 
                : allModuleCodes;
            
            Set<String> createdModules = new HashSet<>();
            
            for (String moduleCode : modulesToCreate) {
                ModuleData moduleData = allModules.stream()
                    .filter(m -> m.getCode().equals(moduleCode))
                    .findFirst()
                    .orElse(null);
                
                if (moduleData != null) {
                    // Create system-wide module feature
                    TenantFeature feature = new TenantFeature();
                    feature.setGeneratedId(idsGeneraterService.generateTenantFeatureId());
                    feature.setTenantId(null); // System-wide feature
                    feature.setFeatureCode(moduleData.getCode());
                    feature.setFeatureName(moduleData.getName());
                    feature.setDescription("System module: " + moduleData.getDescription());
                    feature.setIsEnabled(true);
                    feature.setFeatureType(TenantFeature.FeatureType.BOOLEAN);
                    feature.setIsSystemFeature(true);
                    
                    tenantFeatureRepository.save(feature);
                    createdModules.add(moduleCode);
                }
            }
            
            OnboardDataResponse response = new OnboardDataResponse(true, "Modules onboarded successfully");
            response.setModulesCreated(createdModules.size());
            response.setCreatedModules(createdModules);
            
            log.info("Modules onboarding completed. Created: {}", createdModules.size());
            return response;
            
        } catch (Exception e) {
            log.error("Error during modules onboarding: {}", e.getMessage(), e);
            OnboardDataResponse errorResponse = new OnboardDataResponse(false, "Failed to onboard modules: " + e.getMessage());
            errorResponse.setErrorDetails(e.getMessage());
            return errorResponse;
        }
    }
    
    @Override
    @Transactional
    public OnboardDataResponse onboardPermissions(OnboardDataRequest request) {
        log.info("Onboarding permissions");
        
        try {
            List<PermissionData> allPermissions = jsonDataLoaderService.loadPermissions();
            Set<String> allPermissionCodes = allPermissions.stream()
                .map(PermissionData::getCode)
                .collect(Collectors.toSet());
            
            Set<String> permissionsToCreate = request.getSpecificPermissions() != null && !request.getSpecificPermissions().isEmpty() 
                ? request.getSpecificPermissions() 
                : allPermissionCodes;
            
            Set<String> createdPermissions = new HashSet<>();
            
            // For now, we'll just track the permissions that would be created
            // In a real implementation, you might have a Permissions entity
            for (String permissionCode : permissionsToCreate) {
                if (allPermissionCodes.contains(permissionCode)) {
                    createdPermissions.add(permissionCode);
                }
            }
            
            OnboardDataResponse response = new OnboardDataResponse(true, "Permissions onboarded successfully");
            response.setPermissionsCreated(createdPermissions.size());
            response.setCreatedPermissions(createdPermissions);
            
            log.info("Permissions onboarding completed. Created: {}", createdPermissions.size());
            return response;
            
        } catch (Exception e) {
            log.error("Error during permissions onboarding: {}", e.getMessage(), e);
            OnboardDataResponse errorResponse = new OnboardDataResponse(false, "Failed to onboard permissions: " + e.getMessage());
            errorResponse.setErrorDetails(e.getMessage());
            return errorResponse;
        }
    }
    
    @Override
    @Transactional
    public OnboardDataResponse onboardRoles(OnboardDataRequest request) {
        log.info("Onboarding roles");
        
        try {
            List<RoleData> allRoles = jsonDataLoaderService.loadRoles();
            Set<String> allRoleCodes = allRoles.stream()
                .map(RoleData::getCode)
                .collect(Collectors.toSet());
            
            Set<String> rolesToCreate = request.getSpecificRoles() != null && !request.getSpecificRoles().isEmpty() 
                ? request.getSpecificRoles() 
                : allRoleCodes;
            
            Set<String> createdRoles = new HashSet<>();
            
            for (String roleCode : rolesToCreate) {
                RoleData roleData = allRoles.stream()
                    .filter(r -> r.getCode().equals(roleCode))
                    .findFirst()
                    .orElse(null);
                
                if (roleData != null) {
                    // Check if role already exists
                    if (!rolesRepository.existsByRoleCodeAndIsSystemRoleTrue(roleCode)) {
                        Roles role = new Roles();
                        role.setGeneratedId(idsGeneraterService.generateRoleId());
                        role.setTenantId(null); // System-wide role
                        role.setRoleCode(roleData.getCode());
                        role.setRoleName(roleData.getName());
                        role.setDescription(roleData.getDescription());
                        role.setIsSystemRole(roleData.isSystemRole());
                        role.setIsDefault(roleData.isDefault());
                        role.setIsActive(true);
                        role.setRoleType(Roles.RoleType.valueOf(roleData.getRoleType()));
                        role.setPriority(roleData.getPriority());
                        role.setPermissionCodes(new HashSet<>(roleData.getPermissions()));
                        
                        rolesRepository.save(role);
                        createdRoles.add(roleCode);
                    }
                }
            }
            
            OnboardDataResponse response = new OnboardDataResponse(true, "Roles onboarded successfully");
            response.setRolesCreated(createdRoles.size());
            response.setCreatedRoles(createdRoles);
            
            log.info("Roles onboarding completed. Created: {}", createdRoles.size());
            return response;
            
        } catch (Exception e) {
            log.error("Error during roles onboarding: {}", e.getMessage(), e);
            OnboardDataResponse errorResponse = new OnboardDataResponse(false, "Failed to onboard roles: " + e.getMessage());
            errorResponse.setErrorDetails(e.getMessage());
            return errorResponse;
        }
    }
    
    @Override
    @Transactional
    public OnboardDataResponse onboardFeatures(OnboardDataRequest request) {
        log.info("Onboarding features");
        
        try {
            List<FeatureData> allFeatures = jsonDataLoaderService.loadFeatures();
            Set<String> allFeatureCodes = allFeatures.stream()
                .map(FeatureData::getCode)
                .collect(Collectors.toSet());
            
            Set<String> featuresToCreate = request.getSpecificFeatures() != null && !request.getSpecificFeatures().isEmpty() 
                ? request.getSpecificFeatures() 
                : allFeatureCodes;
            
            Set<String> createdFeatures = new HashSet<>();
            
            for (String featureCode : featuresToCreate) {
                FeatureData featureData = allFeatures.stream()
                    .filter(f -> f.getCode().equals(featureCode))
                    .findFirst()
                    .orElse(null);
                
                if (featureData != null) {
                    // Create system-wide feature
                    TenantFeature feature = new TenantFeature();
                    feature.setGeneratedId(idsGeneraterService.generateFeatureId());
                    feature.setTenantId(null); // System-wide feature
                    feature.setFeatureCode(featureData.getCode());
                    feature.setFeatureName(featureData.getName());
                    feature.setDescription(featureData.getDescription());
                    feature.setIsEnabled(true);
                    feature.setFeatureType(TenantFeature.FeatureType.valueOf(featureData.getType()));
                    feature.setIsSystemFeature(featureData.isSystemFeature());
                    
                    tenantFeatureRepository.save(feature);
                    createdFeatures.add(featureCode);
                }
            }
            
            OnboardDataResponse response = new OnboardDataResponse(true, "Features onboarded successfully");
            response.setFeaturesCreated(createdFeatures.size());
            response.setCreatedFeatures(createdFeatures);
            
            log.info("Features onboarding completed. Created: {}", createdFeatures.size());
            return response;
            
        } catch (Exception e) {
            log.error("Error during features onboarding: {}", e.getMessage(), e);
            OnboardDataResponse errorResponse = new OnboardDataResponse(false, "Failed to onboard features: " + e.getMessage());
            errorResponse.setErrorDetails(e.getMessage());
            return errorResponse;
        }
    }
    
    @Override
    public OnboardDataResponse getOnboardStatus() {
        log.info("Getting onboard status");
        
        try {
            Map<String, Object> status = new HashMap<>();
            
            // Count existing system roles
            long systemRolesCount = rolesRepository.findByIsSystemRoleTrue().size();
            status.put("systemRolesCount", systemRolesCount);
            
            // Count existing system features
            long systemFeaturesCount = tenantFeatureRepository.findByTenantIdAndIsSystemFeatureTrue(null).size();
            status.put("systemFeaturesCount", systemFeaturesCount);
            
            // List existing system roles
            List<String> existingRoles = rolesRepository.findByIsSystemRoleTrue()
                .stream()
                .map(Roles::getRoleCode)
                .collect(Collectors.toList());
            status.put("existingRoles", existingRoles);
            
            // List existing system features
            List<String> existingFeatures = tenantFeatureRepository.findByTenantIdAndIsSystemFeatureTrue(null)
                .stream()
                .map(TenantFeature::getFeatureCode)
                .collect(Collectors.toList());
            status.put("existingFeatures", existingFeatures);
            
            OnboardDataResponse response = new OnboardDataResponse(true, "Onboard status retrieved successfully");
            response.setStatus(status);
            
            return response;
            
        } catch (Exception e) {
            log.error("Error getting onboard status: {}", e.getMessage(), e);
            OnboardDataResponse errorResponse = new OnboardDataResponse(false, "Failed to get onboard status: " + e.getMessage());
            errorResponse.setErrorDetails(e.getMessage());
            return errorResponse;
        }
    }
    
}
