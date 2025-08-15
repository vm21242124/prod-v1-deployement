package com.vm2124.userService.config;

import com.vm2124.userService.model.User;
import com.vm2124.userService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Only initialize if no users exist
        if (userRepository.count() == 0) {
            initializeSampleUsers();
        }
    }
    
    private void initializeSampleUsers() {
        User user1 = new User();
        user1.setUsername("john_doe");
        user1.setEmail("john.doe@example.com");
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setPassword("password123");
        user1.setStatus(User.UserStatus.ACTIVE);
        userRepository.save(user1);
        
        User user2 = new User();
        user2.setUsername("jane_smith");
        user2.setEmail("jane.smith@example.com");
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setPassword("password456");
        user2.setStatus(User.UserStatus.ACTIVE);
        userRepository.save(user2);
        
        User user3 = new User();
        user3.setUsername("admin");
        user3.setEmail("admin@example.com");
        user3.setFirstName("Admin");
        user3.setLastName("User");
        user3.setPassword("admin123");
        user3.setStatus(User.UserStatus.ACTIVE);
        userRepository.save(user3);
        
        System.out.println("Sample users initialized successfully!");
    }
}
