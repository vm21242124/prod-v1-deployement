package com.vm2124.userService.repository;

import com.vm2124.userService.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, UUID> {
    
    Optional<Tenant> findByGeneratedId(String generatedId);
    
    Optional<Tenant> findByTenantCode(String tenantCode);
    
    Optional<Tenant> findByDomain(String domain);
    
    boolean existsByGeneratedId(String generatedId);
    
    boolean existsByTenantCode(String tenantCode);
    
    boolean existsByDomain(String domain);
    
    Optional<Tenant> findByCustomDomain(String customDomain);
}
