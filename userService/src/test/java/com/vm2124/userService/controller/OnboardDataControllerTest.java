package com.vm2124.userService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vm2124.userService.dto.OnboardDataRequest;
import com.vm2124.userService.dto.OnboardDataResponse;
import com.vm2124.userService.service.OnboardDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OnboardDataController.class)
public class OnboardDataControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private OnboardDataService onboardDataService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    public void testInitializeSystemData_Success() throws Exception {
        // Given
        OnboardDataRequest request = new OnboardDataRequest();
        request.setOnboardModules(true);
        request.setOnboardPermissions(true);
        request.setOnboardRoles(true);
        request.setOnboardFeatures(true);
        
        OnboardDataResponse response = new OnboardDataResponse();
        response.setSuccess(true);
        response.setMessage("System data initialized successfully. Total items created: 25");
        response.setModulesCreated(5);
        response.setRolesCreated(5);
        response.setPermissionsCreated(10);
        response.setFeaturesCreated(5);
        response.setCreatedModules(Set.of("USER_MANAGEMENT", "ROLE_MANAGEMENT", "TENANT_CONFIGURATION", "AUDIT_LOGS", "REPORTING"));
        response.setCreatedRoles(Set.of("SUPER_ADMIN", "SYSTEM_ADMIN", "TENANT_ADMIN", "TENANT_USER", "GUEST"));
        
        when(onboardDataService.initializeSystemData(any(OnboardDataRequest.class))).thenReturn(response);
        
        // When & Then
        mockMvc.perform(post("/api/v1/onboard-data/initialize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.modulesCreated").value(5))
                .andExpect(jsonPath("$.rolesCreated").value(5))
                .andExpect(jsonPath("$.permissionsCreated").value(10))
                .andExpect(jsonPath("$.featuresCreated").value(5));
    }
    
    @Test
    public void testOnboardModules_Success() throws Exception {
        // Given
        OnboardDataRequest request = new OnboardDataRequest();
        request.setSpecificModules(Set.of("USER_MANAGEMENT", "ROLE_MANAGEMENT"));
        
        OnboardDataResponse response = new OnboardDataResponse();
        response.setSuccess(true);
        response.setMessage("Modules onboarded successfully");
        response.setModulesCreated(2);
        response.setCreatedModules(Set.of("USER_MANAGEMENT", "ROLE_MANAGEMENT"));
        
        when(onboardDataService.onboardModules(any(OnboardDataRequest.class))).thenReturn(response);
        
        // When & Then
        mockMvc.perform(post("/api/v1/onboard-data/modules")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.modulesCreated").value(2))
                .andExpect(jsonPath("$.createdModules").isArray());
    }
    
    @Test
    public void testOnboardRoles_Success() throws Exception {
        // Given
        OnboardDataRequest request = new OnboardDataRequest();
        request.setSpecificRoles(Set.of("SUPER_ADMIN", "TENANT_ADMIN"));
        
        OnboardDataResponse response = new OnboardDataResponse();
        response.setSuccess(true);
        response.setMessage("Roles onboarded successfully");
        response.setRolesCreated(2);
        response.setCreatedRoles(Set.of("SUPER_ADMIN", "TENANT_ADMIN"));
        
        when(onboardDataService.onboardRoles(any(OnboardDataRequest.class))).thenReturn(response);
        
        // When & Then
        mockMvc.perform(post("/api/v1/onboard-data/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.rolesCreated").value(2))
                .andExpect(jsonPath("$.createdRoles").isArray());
    }
    
    @Test
    public void testOnboardFeatures_Success() throws Exception {
        // Given
        OnboardDataRequest request = new OnboardDataRequest();
        request.setSpecificFeatures(Set.of("SECURITY", "BACKUP_RESTORE"));
        
        OnboardDataResponse response = new OnboardDataResponse();
        response.setSuccess(true);
        response.setMessage("Features onboarded successfully");
        response.setFeaturesCreated(2);
        response.setCreatedFeatures(Set.of("SECURITY", "BACKUP_RESTORE"));
        
        when(onboardDataService.onboardFeatures(any(OnboardDataRequest.class))).thenReturn(response);
        
        // When & Then
        mockMvc.perform(post("/api/v1/onboard-data/features")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.featuresCreated").value(2))
                .andExpect(jsonPath("$.createdFeatures").isArray());
    }
    
    @Test
    public void testGetOnboardStatus_Success() throws Exception {
        // Given
        OnboardDataResponse response = new OnboardDataResponse();
        response.setSuccess(true);
        response.setMessage("Onboard status retrieved successfully");
        response.setStatus(java.util.Map.of(
            "systemRolesCount", 5,
            "systemFeaturesCount", 15,
            "existingRoles", Set.of("SUPER_ADMIN", "SYSTEM_ADMIN", "TENANT_ADMIN"),
            "existingFeatures", Set.of("USER_MANAGEMENT", "ROLE_MANAGEMENT", "SECURITY")
        ));
        
        when(onboardDataService.getOnboardStatus()).thenReturn(response);
        
        // When & Then
        mockMvc.perform(get("/api/v1/onboard-data/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status.systemRolesCount").value(5))
                .andExpect(jsonPath("$.status.systemFeaturesCount").value(15));
    }
    
    @Test
    public void testInitializeSystemData_Failure() throws Exception {
        // Given
        OnboardDataRequest request = new OnboardDataRequest();
        
        OnboardDataResponse response = new OnboardDataResponse();
        response.setSuccess(false);
        response.setMessage("Database connection failed");
        response.setErrorDetails("Connection timeout");
        
        when(onboardDataService.initializeSystemData(any(OnboardDataRequest.class))).thenReturn(response);
        
        // When & Then
        mockMvc.perform(post("/api/v1/onboard-data/initialize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Database connection failed"));
    }
}
