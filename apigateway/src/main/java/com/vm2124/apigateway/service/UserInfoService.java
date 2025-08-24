package com.vm2124.apigateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserInfoService {

    private final WebClient.Builder webClientBuilder;

    public Mono<Map> getUserInfoFromUserService(String token) {
        return webClientBuilder.build()
                .get()
                .uri("lb://user-service/api/v1/auth/validate")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(Map.class)
                .doOnSuccess(response -> log.debug("Successfully retrieved user info from User Service"))
                .doOnError(error -> log.error("Error retrieving user info from User Service: {}", error.getMessage()))
                .onErrorResume(error -> {
                    log.warn("Failed to get user info from User Service, using token data: {}", error.getMessage());
                    return Mono.empty();
                });
    }
}
