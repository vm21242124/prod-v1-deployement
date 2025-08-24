package com.vm2124.userService.repository;

import com.vm2124.userService.model.UserTenantRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserTenantRoleRepository extends JpaRepository<UserTenantRole, UUID> {
    
    Optional<UserTenantRole> findByGeneratedId(String generatedId);
    
    List<UserTenantRole> findByUserId(String userId);
    
    List<UserTenantRole> findByTenantId(String tenantId);
    
    List<UserTenantRole> findByUserIdAndTenantId(String userId, String tenantId);
    
    Optional<UserTenantRole> findByUserIdAndRoleIdAndTenantId(String userId, String roleId, String tenantId);
    
    List<UserTenantRole> findByUserIdAndTenantIdAndIsActiveTrue(String userId, String tenantId);
    
    Optional<UserTenantRole> findByUserIdAndTenantIdAndIsPrimaryTrue(String userId, String tenantId);
    
    boolean existsByGeneratedId(String generatedId);
    
    List<UserTenantRole> findByUserId(UUID userId);
}
