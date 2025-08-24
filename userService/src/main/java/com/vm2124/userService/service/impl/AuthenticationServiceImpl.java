package com.vm2124.userService.service.impl;

import com.vm2124.userService.dto.LoginRequest;
import com.vm2124.userService.dto.LoginResponse;
import com.vm2124.userService.model.User;
import com.vm2124.userService.model.UserTenantRole;
import com.vm2124.userService.repository.UserRepository;
import com.vm2124.userService.repository.UserTenantRoleRepository;
import com.vm2124.userService.service.AuthenticationService;
import com.vm2124.userService.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final UserTenantRoleRepository userTenantRoleRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        log.info("Attempting login for user: {}", loginRequest.getEmail());
        
        try {
            // Find user by email
            Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());
            if (userOpt.isEmpty()) {
                return new LoginResponse(false, "Invalid email or password", null, null, null, null);
            }

            User user = userOpt.get();
            
            // Check if user is active
            if (user.getStatus() != User.UserStatus.ACTIVE) {
                return new LoginResponse(false, "User account is not active", null, null, null, null);
            }
            
            // Verify password
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return new LoginResponse(false, "Invalid username or password", null, null, null, null);
            }
            
            // Get user roles
            List<UserTenantRole> userRoles = userTenantRoleRepository.findByUserId(user.getId().toString());
            List<String> roleCodes = userRoles.stream()
                .map(UserTenantRole::getRoleId) // roleId contains the generated role ID
                .collect(Collectors.toList());
            
            // Generate JWT token
            String token = jwtService.generateToken(user.getId().toString(), user.getTenantId(), roleCodes);
            
            // Update last login time
            user.setLastLoginAt(LocalDateTime.now());
            userRepository.save(user);
            
            // Build user info for response
            LoginResponse.UserInfo userInfo = buildUserInfo(user, roleCodes);
            
            // Calculate token expiration
            LocalDateTime expiresAt = LocalDateTime.now().plusHours(24); // 24 hours from now
            
            return new LoginResponse(
                true,
                "Login successful",
                token,
                "Bearer",
                expiresAt,
                userInfo
            );
            
        } catch (Exception e) {
            log.error("Error during login for user {}: {}", loginRequest.getEmail(), e.getMessage(), e);
            return new LoginResponse(false, "Login failed: " + e.getMessage(), null, null, null, null);
        }
    }

    private LoginResponse.UserInfo buildUserInfo(User user, List<String> roleCodes) {
        return new LoginResponse.UserInfo(
            user.getId().toString(),
            user.getGeneratedId(),
            user.getUsername(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getStatus() == User.UserStatus.ACTIVE,
            user.getTenantId(),
            user.getTenantId(), // Assuming tenantId is the generated ID
            roleCodes,
            user.getLastLoginAt()
        );
    }
}
