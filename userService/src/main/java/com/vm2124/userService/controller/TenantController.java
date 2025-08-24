package com.vm2124.userService.controller;

import com.vm2124.userService.dto.TenantOnboardingRequest;
import com.vm2124.userService.dto.TenantOnboardingResponse;
import com.vm2124.userService.service.TenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1/tenants")
@RequiredArgsConstructor
@Slf4j
public class TenantController {
    
    private final TenantService tenantService;
    
    @PostMapping("/onboard")
    public ResponseEntity<TenantOnboardingResponse> onboardTenant(
            @RequestBody TenantOnboardingRequest request) {
        
        log.info("Received tenant onboarding request for tenant code: {}", request.getTenantCode());
        
        TenantOnboardingResponse response = tenantService.onboardTenant(request);
        
        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
