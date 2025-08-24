package com.vm2124.userService.repository;

import com.vm2124.userService.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RolesRepository extends JpaRepository<Roles, UUID> {
    
    Optional<Roles> findByGeneratedId(String generatedId);
    
    Optional<Roles> findByRoleCode(String roleCode);
    
    Optional<Roles> findByRoleCodeAndTenantId(String roleCode, String tenantId);
    
    List<Roles> findByTenantId(String tenantId);
    
    List<Roles> findByIsSystemRoleTrue();
    
    Optional<Roles> findByRoleCodeAndIsSystemRoleTrue(String roleCode);
    
    List<Roles> findByTenantIdAndIsActiveTrue(String tenantId);
    
    boolean existsByGeneratedId(String generatedId);
    
    boolean existsByRoleCodeAndIsSystemRoleTrue(String roleCode);
}
