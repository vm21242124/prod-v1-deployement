package com.vm2124.userService.service;

import com.vm2124.userService.dto.OnboardDataRequest;
import com.vm2124.userService.dto.OnboardDataResponse;

public interface OnboardDataService {
    
    OnboardDataResponse initializeSystemData(OnboardDataRequest request);
    
    OnboardDataResponse onboardModules(OnboardDataRequest request);
    
    OnboardDataResponse onboardPermissions(OnboardDataRequest request);
    
    OnboardDataResponse onboardRoles(OnboardDataRequest request);
    
    OnboardDataResponse onboardFeatures(OnboardDataRequest request);
    
    OnboardDataResponse getOnboardStatus();
}
