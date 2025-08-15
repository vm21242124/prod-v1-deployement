package com.vm2124.apigateway.controller;

import com.vm2124.apigateway.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<Map<String, Object>>> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        if (username == null || password == null) {
            return Mono.just(ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Username and password are required")));
        }

        return authenticationService.authenticate(username, password)
                .map(result -> {
                    if ((Boolean) result.get("success")) {
                        return ResponseEntity.ok(result);
                    } else {
                        return ResponseEntity.status(401).body(result);
                    }
                });
    }

    @GetMapping("/validate")
    public Mono<ResponseEntity<Map<String, Object>>> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Mono.just(ResponseEntity.status(401)
                    .body(Map.of("valid", false, "message", "Invalid token format")));
        }

        String token = authHeader.substring(7);
        // The JWT validation is already done by the filter, so if we reach here, the token is valid
        return Mono.just(ResponseEntity.ok(Map.of("valid", true, "message", "Token is valid")));
    }
}
