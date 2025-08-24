package com.vm2124.userService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vm2124.userService.dto.TenantOnboardingRequest;
import com.vm2124.userService.dto.TenantOnboardingResponse;
import com.vm2124.userService.model.Tenant;
import com.vm2124.userService.service.TenantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TenantController.class)
public class TenantControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private TenantService tenantService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    public void testOnboardTenant_Success() throws Exception {
        // Given
        TenantOnboardingRequest request = new TenantOnboardingRequest();
        request.setTenantCode("TEST001");
        request.setName("Test Company");
        request.setDomain("test.com");
        request.setDescription("Test tenant for onboarding");
        request.setSubscriptionPlan(Tenant.SubscriptionPlan.BASIC);
        request.setMaxUsers(50);
        request.setAdminUsername("admin");
        request.setAdminEmail("admin@test.com");
        request.setAdminFirstName("Admin");
        request.setAdminLastName("User");
        request.setAdminPassword("password123");
        request.setAdminPhoneNumber("1234567890");
        request.setAdminDepartment("IT");
        request.setAdminJobTitle("System Administrator");
        request.setEnabledModules(Set.of("USER_MANAGEMENT", "ROLE_MANAGEMENT"));
        
        TenantOnboardingResponse response = new TenantOnboardingResponse();
        response.setTenantId(UUID.randomUUID());
        response.setTenantGeneratedId("TNT1234567");
        response.setTenantCode("TEST001");
        response.setTenantName("Test Company");
        response.setDomain("test.com");
        response.setStatus("ACTIVE");
        response.setAdminUserId(UUID.randomUUID());
        response.setAdminGeneratedId("USR1234567");
        response.setAdminUsername("admin");
        response.setAdminEmail("admin@test.com");
        response.setAdminFullName("Admin User");
        response.setEnabledModules(Set.of("USER_MANAGEMENT", "ROLE_MANAGEMENT"));
        response.setSuccess(true);
        response.setMessage("Tenant onboarded successfully");
        
        when(tenantService.onboardTenant(any(TenantOnboardingRequest.class))).thenReturn(response);
        
        // When & Then
        mockMvc.perform(post("/api/v1/tenants/onboard")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.tenantCode").value("TEST001"))
                .andExpect(jsonPath("$.adminUsername").value("admin"))
                .andExpect(jsonPath("$.enabledModules").isArray());
    }
    
    @Test
    public void testOnboardTenant_Failure() throws Exception {
        // Given
        TenantOnboardingRequest request = new TenantOnboardingRequest();
        request.setTenantCode("TEST001");
        request.setName("Test Company");
        request.setDomain("test.com");
        request.setAdminUsername("admin");
        request.setAdminEmail("admin@test.com");
        request.setAdminFirstName("Admin");
        request.setAdminLastName("User");
        request.setAdminPassword("password123");
        
        TenantOnboardingResponse response = new TenantOnboardingResponse();
        response.setSuccess(false);
        response.setMessage("Tenant code already exists");
        
        when(tenantService.onboardTenant(any(TenantOnboardingRequest.class))).thenReturn(response);
        
        // When & Then
        mockMvc.perform(post("/api/v1/tenants/onboard")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Tenant code already exists"));
    }
}
