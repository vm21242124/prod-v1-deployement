package com.vm2124.userService.controller;

import com.vm2124.userService.dto.UserDto;
import com.vm2124.userService.model.User;
import com.vm2124.userService.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "User Service");
        response.put("timestamp", System.currentTimeMillis());
        response.put("message", "User service is running");
        return ResponseEntity.ok(response);
    }
    
    // Test endpoint
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> test() {
        System.out.println("User service test endpoint");
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User service test endpoint");
        response.put("status", "SUCCESS");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
    
    // Get all users
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Get user by username
    @GetMapping("/username/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Create new user
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody User user) {
        // Check if username already exists
        if (userService.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().build();
        }
        
        // Check if email already exists
        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().build();
        }
        
        UserDto createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    
    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return userService.updateUser(id, userDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    // Check if username exists
    @GetMapping("/check-username/{username}")
    public ResponseEntity<Map<String, Object>> checkUsername(@PathVariable String username) {
        Map<String, Object> response = new HashMap<>();
        response.put("username", username);
        response.put("exists", userService.existsByUsername(username));
        return ResponseEntity.ok(response);
    }
    
    // Check if email exists
    @GetMapping("/check-email/{email}")
    public ResponseEntity<Map<String, Object>> checkEmail(@PathVariable String email) {
        Map<String, Object> response = new HashMap<>();
        response.put("email", email);
        response.put("exists", userService.existsByEmail(email));
        return ResponseEntity.ok(response);
    }
    
    // Get user count
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getUserCount() {
        Map<String, Object> response = new HashMap<>();
        response.put("count", userService.getAllUsers().size());
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
    
    // Simulate slow response for testing circuit breaker
    @GetMapping("/slow")
    public ResponseEntity<Map<String, Object>> slowResponse() throws InterruptedException {
        Thread.sleep(5000); // Simulate 5 second delay
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Slow response completed");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
    
    // Simulate error for testing circuit breaker
    @GetMapping("/error")
    public ResponseEntity<Map<String, Object>> simulateError() {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Simulated error");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
