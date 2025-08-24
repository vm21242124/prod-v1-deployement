package com.vm2124.userService.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vm2124.userService.dto.FeatureData;
import com.vm2124.userService.dto.ModuleData;
import com.vm2124.userService.dto.PermissionData;
import com.vm2124.userService.dto.RoleData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JsonDataLoaderService {
    
    private final ObjectMapper objectMapper;
    
    public List<ModuleData> loadModules() {
        return loadJsonData("data/modules.json", new TypeReference<List<ModuleData>>() {});
    }
    
    public List<PermissionData> loadPermissions() {
        return loadJsonData("data/permissions.json", new TypeReference<List<PermissionData>>() {});
    }
    
    public List<RoleData> loadRoles() {
        return loadJsonData("data/roles.json", new TypeReference<List<RoleData>>() {});
    }
    
    public List<FeatureData> loadFeatures() {
        return loadJsonData("data/features.json", new TypeReference<List<FeatureData>>() {});
    }
    
    private <T> List<T> loadJsonData(String resourcePath, TypeReference<List<T>> typeReference) {
        try {
            ClassPathResource resource = new ClassPathResource(resourcePath);
            InputStream inputStream = resource.getInputStream();
            return objectMapper.readValue(inputStream, typeReference);
        } catch (IOException e) {
            log.error("Error loading JSON data from {}: {}", resourcePath, e.getMessage(), e);
            throw new RuntimeException("Failed to load JSON data from " + resourcePath, e);
        }
    }
}
