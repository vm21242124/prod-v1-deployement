package com.vm2124.userService.controller;

import com.vm2124.userService.dto.OnboardDataRequest;
import com.vm2124.userService.dto.OnboardDataResponse;
import com.vm2124.userService.service.OnboardDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/onboard-data")
@RequiredArgsConstructor
@Slf4j
public class OnboardDataController {
    
    private final OnboardDataService onboardDataService;
    
    @PostMapping("/initialize")
    public ResponseEntity<OnboardDataResponse> initializeSystemData(
            @RequestBody OnboardDataRequest request) {
        
        log.info("Received system data initialization request");
        
        OnboardDataResponse response = onboardDataService.initializeSystemData(request);
        
        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PostMapping("/modules")
    public ResponseEntity<OnboardDataResponse> onboardModules(
            @RequestBody OnboardDataRequest request) {
        
        log.info("Received modules onboarding request");
        
        OnboardDataResponse response = onboardDataService.onboardModules(request);
        
        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PostMapping("/permissions")
    public ResponseEntity<OnboardDataResponse> onboardPermissions(
            @RequestBody OnboardDataRequest request) {
        
        log.info("Received permissions onboarding request");
        
        OnboardDataResponse response = onboardDataService.onboardPermissions(request);
        
        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PostMapping("/roles")
    public ResponseEntity<OnboardDataResponse> onboardRoles(
            @RequestBody OnboardDataRequest request) {
        
        log.info("Received roles onboarding request");
        
        OnboardDataResponse response = onboardDataService.onboardRoles(request);
        
        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PostMapping("/features")
    public ResponseEntity<OnboardDataResponse> onboardFeatures(
            @RequestBody OnboardDataRequest request) {
        
        log.info("Received features onboarding request");
        
        OnboardDataResponse response = onboardDataService.onboardFeatures(request);
        
        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @GetMapping("/status")
    public ResponseEntity<OnboardDataResponse> getOnboardStatus() {
        
        log.info("Received onboard status request");
        
        OnboardDataResponse response = onboardDataService.getOnboardStatus();
        
        return ResponseEntity.ok(response);
    }
}
