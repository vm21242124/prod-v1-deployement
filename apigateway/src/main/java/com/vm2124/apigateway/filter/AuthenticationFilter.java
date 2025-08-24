package com.vm2124.apigateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vm2124.apigateway.service.JwtService;
import com.vm2124.apigateway.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final UserInfoService userInfoService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();

        // Skip authentication for public endpoints
        if (isPublicEndpoint(path)) {
            log.debug("Skipping authentication for public endpoint: {}", path);
            return chain.filter(exchange);
        }

        // Extract token from Authorization header
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            log.warn("Missing or invalid Authorization header for path: {}", path);
            return unauthorizedResponse(exchange, "Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        
        try {
            // Validate token
            if (!jwtService.validateToken(token)) {
                log.warn("Invalid JWT token for path: {}", path);
                return unauthorizedResponse(exchange, "Invalid JWT token");
            }

            // Extract user information from token
            String userId = jwtService.extractUserId(token);
            String tenantId = jwtService.extractTenantId(token);
            List<String> roles = jwtService.extractRoles(token);

            // Call User Service to get comprehensive user information
            return userInfoService.getUserInfoFromUserService(token)
                    .flatMap(userInfo -> {
                        // Add user information as headers
                        ServerHttpRequest modifiedRequest = addUserHeaders(request, userInfo);
                        
                        log.debug("Authentication successful for user: {} on path: {}", userId, path);
                        return chain.filter(exchange.mutate().request(modifiedRequest).build());
                    })
                    .onErrorResume(e -> {
                        log.error("Error getting user info from User Service: {}", e.getMessage());
                        return unauthorizedResponse(exchange, "Error retrieving user information");
                    });

        } catch (Exception e) {
            log.error("Authentication error for path {}: {}", path, e.getMessage(), e);
            return unauthorizedResponse(exchange, "Authentication failed: " + e.getMessage());
        }
    }

    private boolean isPublicEndpoint(String path) {
        return path.startsWith("/api/auth/login") ||
               path.startsWith("/api/auth/register") ||
               path.startsWith("/actuator/") ||
               path.startsWith("/health") ||
               path.startsWith("/test/") ||
               path.equals("/favicon.ico");
    }



    private ServerHttpRequest addUserHeaders(ServerHttpRequest request, Map<String, Object> userInfo) {
        ServerHttpRequest.Builder builder = request.mutate();

        // Extract user data from User Service response
        @SuppressWarnings("unchecked")
        Map<String, Object> userData = (Map<String, Object>) userInfo.get("user");
        
        if (userData != null) {
            // Add user information as headers from User Service response
            builder.header("X-User-ID", (String) userData.get("id"));
            builder.header("X-User-Generated-ID", (String) userData.get("generatedId"));
            builder.header("X-Username", (String) userData.get("username"));
            builder.header("X-Email", (String) userData.get("email"));
            builder.header("X-First-Name", (String) userData.get("firstName"));
            builder.header("X-Last-Name", (String) userData.get("lastName"));
            builder.header("X-Is-Active", String.valueOf(userData.get("isActive")));
            builder.header("X-Tenant-ID", (String) userData.get("tenantId"));
            builder.header("X-Tenant-Generated-ID", (String) userData.get("tenantGeneratedId"));
        }

        // Add roles as JSON array
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> rolesData = (List<Map<String, Object>>) userInfo.get("roles");
            if (rolesData != null) {
                List<String> roleCodes = rolesData.stream()
                    .map(role -> (String) role.get("roleCode"))
                    .toList();
                String rolesJson = objectMapper.writeValueAsString(roleCodes);
                builder.header("X-User-Roles", rolesJson);
            }
        } catch (Exception e) {
            log.warn("Failed to serialize roles to JSON: {}", e.getMessage());
        }

        // Add permissions as JSON array
        try {
            @SuppressWarnings("unchecked")
            List<String> permissions = (List<String>) userInfo.get("permissions");
            if (permissions != null) {
                String permissionsJson = objectMapper.writeValueAsString(permissions);
                builder.header("X-User-Permissions", permissionsJson);
            }
        } catch (Exception e) {
            log.warn("Failed to serialize permissions to JSON: {}", e.getMessage());
        }

        return builder.build();
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");
        
        String errorResponse = String.format("{\"error\":\"Unauthorized\",\"message\":\"%s\",\"timestamp\":\"%s\"}", 
            message, java.time.Instant.now());
        
        return exchange.getResponse().writeWith(
            Mono.just(exchange.getResponse().bufferFactory().wrap(errorResponse.getBytes()))
        );
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100; // Run after CORS but before other filters
    }
}
