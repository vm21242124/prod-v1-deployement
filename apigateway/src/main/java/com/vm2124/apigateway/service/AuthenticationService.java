package com.vm2124.apigateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class AuthenticationService {

    private final WebClient webClient;
    private final JwtService jwtService;

    @Autowired
    public AuthenticationService(JwtService jwtService) {
        this.jwtService = jwtService;
        this.webClient = WebClient.builder()
                .baseUrl("http://user-service:8081")
                .build();
    }

    public Mono<Map<String, Object>> authenticate(String username, String password) {
        return webClient.post()
                .uri("/api/auth/login")
                .bodyValue(Map.of("username", username, "password", password))
                .retrieve()
                .bodyToMono(Map.class)
                .flatMap(response -> {
                    if (response.containsKey("success") && (Boolean) response.get("success")) {
                        // Generate JWT token
                        @SuppressWarnings("unchecked")
                        Map<String, Object> user = (Map<String, Object>) response.get("user");
                        @SuppressWarnings("unchecked")
                        java.util.List<String> roles = (java.util.List<String>) user.get("roles");
                        
                        String token = jwtService.generateToken(username, roles);
                        
                        return Mono.just(Map.of(
                            "success", true,
                            "token", token,
                            "user", user
                        ));
                    } else {
                        return Mono.just(Map.of(
                            "success", false,
                            "message", response.getOrDefault("message", "Authentication failed")
                        ));
                    }
                })
                .onErrorReturn(Map.of(
                    "success", false,
                    "message", "Service unavailable"
                ));
    }

    public Mono<Map<String, Object>> validateUser(String username) {
        return webClient.get()
                .uri("/api/users/username/{username}", username)
                .retrieve()
                .bodyToMono(Map.class)
                .map(user -> Map.of("valid", true, "user", user))
                .onErrorReturn(Map.of("valid", false, "message", "User not found"));
    }
}
