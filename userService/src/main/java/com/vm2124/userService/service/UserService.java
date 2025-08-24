package com.vm2124.userService.service;

import com.vm2124.userService.model.User;

import java.util.UUID;

public interface UserService {
    
    User createUser(User user);
    
    User createAdminUser(String tenantId, String username, String email, String firstName, 
                        String lastName, String password, String phoneNumber, 
                        String department, String jobTitle);
    
}
