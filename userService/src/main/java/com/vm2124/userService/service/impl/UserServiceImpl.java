package com.vm2124.userService.service.impl;

import com.vm2124.userService.model.User;
import com.vm2124.userService.repository.UserRepository;
import com.vm2124.userService.service.IdsGeneraterService;
import com.vm2124.userService.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IdsGeneraterService idsGeneraterService;
    
    @Override
    @Transactional
    public User createUser(User user) {
        log.info("Creating user: {} for tenant: {}", user.getUsername(), user.getTenantId());
        
        // Validate user data
        if (userRepository.existsByUsernameAndTenantId(user.getUsername(), user.getTenantId())) {
            throw new RuntimeException("Username already exists in tenant: " + user.getUsername());
        }
        
        if (userRepository.existsByEmailAndTenantId(user.getEmail(), user.getTenantId())) {
            throw new RuntimeException("Email already exists in tenant: " + user.getEmail());
        }
        
        // Set generated ID if not already set
        if (user.getGeneratedId() == null) {
            user.setGeneratedId(idsGeneraterService.generateUserId());
        }
        
        // Encode password if not already encoded
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        // Set default values
        if (user.getStatus() == null) {
            user.setStatus(User.UserStatus.ACTIVE);
        }
        
        if (user.getPasswordChangedAt() == null) {
            user.setPasswordChangedAt(LocalDateTime.now());
        }
        
        User savedUser = userRepository.save(user);
        log.info("User created successfully: {}", savedUser.getId());
        
        return savedUser;
    }
    
    @Override
    @Transactional
    public User createAdminUser(String tenantId, String username, String email, String firstName, 
                               String lastName, String password, String phoneNumber, 
                               String department, String jobTitle) {
        log.info("Creating admin user: {} for tenant: {}", username, tenantId);
        
        User adminUser = new User();
        adminUser.setGeneratedId(idsGeneraterService.generateUserId());
        adminUser.setTenantId(tenantId);
        adminUser.setUsername(username);
        adminUser.setEmail(email);
        adminUser.setFirstName(firstName);
        adminUser.setLastName(lastName);
        adminUser.setPassword(passwordEncoder.encode(password));
        adminUser.setPhoneNumber(phoneNumber);
        adminUser.setDepartment(department);
        adminUser.setJobTitle(jobTitle);
        adminUser.setStatus(User.UserStatus.ACTIVE);
        adminUser.setPasswordChangedAt(LocalDateTime.now());
        
        return createUser(adminUser);
    }
}
