package com.vm2124.userService.controller;

import com.vm2124.userService.dto.LoginRequest;
import com.vm2124.userService.dto.LoginResponse;
import com.vm2124.userService.dto.UserInfoDto;
import com.vm2124.userService.service.AuthenticationService;
import com.vm2124.userService.service.TokenValidationService;
import com.vm2124.userService.service.UserContextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AuthController {

    private final TokenValidationService tokenValidationService;
    private final AuthenticationService authenticationService;
    private final UserContextService userContextService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        log.info("Login attempt for user: {}", loginRequest.getEmail());
        
        try {
            LoginResponse loginResponse = authenticationService.login(loginRequest);
            
            if (loginResponse.isSuccess()) {
                return ResponseEntity.ok(loginResponse);
            } else {
                return ResponseEntity.status(401).body(loginResponse);
            }
            
        } catch (Exception e) {
            log.error("Error during login: {}", e.getMessage(), e);
            return ResponseEntity.status(500)
                    .body(new LoginResponse(false, "Login failed: " + e.getMessage(), null, null, null, null));
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<UserInfoDto> validateToken(@RequestHeader("Authorization") String authHeader) {
        log.info("Validating token and fetching comprehensive user information");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401)
                    .body(new UserInfoDto(false, "Invalid token format", null, null, null, null));
        }

        String token = authHeader.substring(7);
        
        try {
            UserInfoDto userInfo = tokenValidationService.validateTokenAndGetUserInfo(token);
            
            if (userInfo.isSuccess()) {
                return ResponseEntity.ok(userInfo);
            } else {
                return ResponseEntity.status(401).body(userInfo);
            }
            
        } catch (Exception e) {
            log.error("Error validating token: {}", e.getMessage(), e);
            return ResponseEntity.status(500)
                    .body(new UserInfoDto(false, "Error validating token: " + e.getMessage(), null, null, null, null));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfoDto> getCurrentUserInfo() {
        log.info("Getting current user information from context");
        
        try {
            UserInfoDto userInfo = userContextService.toUserInfoDto();
            
            if (userInfo.isSuccess()) {
                return ResponseEntity.ok(userInfo);
            } else {
                return ResponseEntity.status(401).body(userInfo);
            }
            
        } catch (Exception e) {
            log.error("Error getting current user info: {}", e.getMessage(), e);
            return ResponseEntity.status(500)
                    .body(new UserInfoDto(false, "Error getting current user info: " + e.getMessage(), null, null, null, null));
        }
    }
}
