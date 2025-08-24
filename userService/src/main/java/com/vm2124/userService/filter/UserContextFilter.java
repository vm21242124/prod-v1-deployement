package com.vm2124.userService.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vm2124.userService.dto.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserContextFilter extends OncePerRequestFilter {

    private final UserContext userContext;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        try {
            // Extract user information from headers (set by API Gateway)
            extractUserInfoFromHeaders(request);
            
            // Continue with the filter chain
            filterChain.doFilter(request, response);
            
        } catch (Exception e) {
            log.error("Error extracting user context from headers: {}", e.getMessage(), e);
            // Continue with the filter chain even if extraction fails
            filterChain.doFilter(request, response);
        }
    }

    private void extractUserInfoFromHeaders(HttpServletRequest request) {
        // Extract user ID
        String userId = request.getHeader("X-User-ID");
        if (userId != null && !userId.isEmpty()) {
            userContext.setUserId(userId);
        }

        // Extract user generated ID
        String userGeneratedId = request.getHeader("X-User-Generated-ID");
        if (userGeneratedId != null && !userGeneratedId.isEmpty()) {
            userContext.setUserGeneratedId(userGeneratedId);
        }

        // Extract username
        String username = request.getHeader("X-Username");
        if (username != null && !username.isEmpty()) {
            userContext.setUsername(username);
        }

        // Extract email
        String email = request.getHeader("X-Email");
        if (email != null && !email.isEmpty()) {
            userContext.setEmail(email);
        }

        // Extract first name
        String firstName = request.getHeader("X-First-Name");
        if (firstName != null && !firstName.isEmpty()) {
            userContext.setFirstName(firstName);
        }

        // Extract last name
        String lastName = request.getHeader("X-Last-Name");
        if (lastName != null && !lastName.isEmpty()) {
            userContext.setLastName(lastName);
        }

        // Extract active status
        String isActive = request.getHeader("X-Is-Active");
        if (isActive != null && !isActive.isEmpty()) {
            userContext.setActive(Boolean.parseBoolean(isActive));
        }

        // Extract tenant ID
        String tenantId = request.getHeader("X-Tenant-ID");
        if (tenantId != null && !tenantId.isEmpty()) {
            userContext.setTenantId(tenantId);
        }

        // Extract tenant generated ID
        String tenantGeneratedId = request.getHeader("X-Tenant-Generated-ID");
        if (tenantGeneratedId != null && !tenantGeneratedId.isEmpty()) {
            userContext.setTenantGeneratedId(tenantGeneratedId);
        }

        // Extract roles (JSON array)
        String rolesHeader = request.getHeader("X-User-Roles");
        if (rolesHeader != null && !rolesHeader.isEmpty()) {
            try {
                List<String> roles = objectMapper.readValue(rolesHeader, new TypeReference<List<String>>() {});
                userContext.setRoles(roles);
            } catch (JsonProcessingException e) {
                log.warn("Failed to parse user roles from header: {}", rolesHeader, e);
            }
        }

        // Extract permissions (JSON array)
        String permissionsHeader = request.getHeader("X-User-Permissions");
        if (permissionsHeader != null && !permissionsHeader.isEmpty()) {
            try {
                List<String> permissions = objectMapper.readValue(permissionsHeader, new TypeReference<List<String>>() {});
                userContext.setPermissions(permissions);
            } catch (JsonProcessingException e) {
                log.warn("Failed to parse user permissions from header: {}", permissionsHeader, e);
            }
        }

        // Log the extracted user context for debugging
        if (userContext.isAuthenticated()) {
            log.debug("User context extracted - User ID: {}, Tenant ID: {}, Roles: {}", 
                userContext.getUserId(), userContext.getTenantId(), userContext.getRoles());
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Skip filtering for certain paths (health checks, actuator endpoints, etc.)
        String path = request.getRequestURI();
        return path.startsWith("/actuator/") || 
               path.startsWith("/health") || 
               path.startsWith("/error") ||
               path.equals("/favicon.ico");
    }
}
