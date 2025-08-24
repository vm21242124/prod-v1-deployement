package com.vm2124.userService.service;

import com.vm2124.userService.dto.TenantOnboardingRequest;
import com.vm2124.userService.dto.TenantOnboardingResponse;

public interface TenantService {
    
    TenantOnboardingResponse onboardTenant(TenantOnboardingRequest request);
    
}
