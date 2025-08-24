package com.vm2124.userService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private boolean success;
    private String message;
    private String token;
    private String tokenType;
    private LocalDateTime expiresAt;
    private UserInfo user;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private String id;
        private String generatedId;
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private boolean isActive;
        private String tenantId;
        private String tenantGeneratedId;
        private List<String> roles;
        private LocalDateTime lastLoginAt;
    }
}
