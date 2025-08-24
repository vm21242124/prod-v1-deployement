package com.vm2124.apigateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
@Slf4j
public class FallbackController {

    @GetMapping("/user-service")
    public Mono<ResponseEntity<Map<String, Object>>> userServiceFallback() {
        log.warn("User Service is unavailable, returning fallback response");
        
        Map<String, Object> fallbackResponse = Map.of(
            "error", "Service Unavailable",
            "message", "User Service is currently unavailable. Please try again later.",
            "timestamp", LocalDateTime.now().toString(),
            "service", "user-service"
        );
        
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(fallbackResponse));
    }

    @GetMapping("/auth-service")
    public Mono<ResponseEntity<Map<String, Object>>> authServiceFallback() {
        log.warn("Auth Service is unavailable, returning fallback response");
        
        Map<String, Object> fallbackResponse = Map.of(
            "error", "Service Unavailable",
            "message", "Authentication Service is currently unavailable. Please try again later.",
            "timestamp", LocalDateTime.now().toString(),
            "service", "auth-service"
        );
        
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(fallbackResponse));
    }

    @GetMapping("/product-service")
    public Mono<ResponseEntity<Map<String, Object>>> productServiceFallback() {
        log.warn("Product Service is unavailable, returning fallback response");
        
        Map<String, Object> fallbackResponse = Map.of(
            "error", "Service Unavailable",
            "message", "Product Service is currently unavailable. Please try again later.",
            "timestamp", LocalDateTime.now().toString(),
            "service", "product-service"
        );
        
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(fallbackResponse));
    }

    @GetMapping("/order-service")
    public Mono<ResponseEntity<Map<String, Object>>> orderServiceFallback() {
        log.warn("Order Service is unavailable, returning fallback response");
        
        Map<String, Object> fallbackResponse = Map.of(
            "error", "Service Unavailable",
            "message", "Order Service is currently unavailable. Please try again later.",
            "timestamp", LocalDateTime.now().toString(),
            "service", "order-service"
        );
        
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(fallbackResponse));
    }
}
