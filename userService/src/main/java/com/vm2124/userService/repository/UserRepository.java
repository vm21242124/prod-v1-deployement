package com.vm2124.userService.repository;

import com.vm2124.userService.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    
    Optional<User> findByGeneratedId(String generatedId);
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByUsernameAndTenantId(String username, String tenantId);
    
    Optional<User> findByEmailAndTenantId(String email, String tenantId);
    
    List<User> findByTenantId(String tenantId);
    
    boolean existsByGeneratedId(String generatedId);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    boolean existsByUsernameAndTenantId(String username, String tenantId);
    
    boolean existsByEmailAndTenantId(String email, String tenantId);
}
