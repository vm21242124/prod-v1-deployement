package com.vm2124.apigateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping("/status")
    public Mono<ResponseEntity<Map<String, Object>>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("service", "API Gateway");
        status.put("timestamp", System.currentTimeMillis());
        status.put("version", "1.0.0");
        
        return Mono.just(ResponseEntity.ok(status));
    }

    @GetMapping("/ready")
    public Mono<ResponseEntity<Map<String, Object>>> getReadiness() {
        Map<String, Object> readiness = new HashMap<>();
        readiness.put("status", "READY");
        readiness.put("message", "API Gateway is ready to accept requests");
        readiness.put("timestamp", System.currentTimeMillis());
        
        return Mono.just(ResponseEntity.ok(readiness));
    }

    @GetMapping("/live")
    public Mono<ResponseEntity<Map<String, Object>>> getLiveness() {
        Map<String, Object> liveness = new HashMap<>();
        liveness.put("status", "ALIVE");
        liveness.put("message", "API Gateway is running");
        liveness.put("timestamp", System.currentTimeMillis());
        
        return Mono.just(ResponseEntity.ok(liveness));
    }
}
