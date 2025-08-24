package com.vm2124.userService.repository;

import com.vm2124.userService.model.TenantFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TenantFeatureRepository extends JpaRepository<TenantFeature, UUID> {
    
    Optional<TenantFeature> findByGeneratedId(String generatedId);
    
    List<TenantFeature> findByTenantId(String tenantId);
    
    List<TenantFeature> findByTenantIdAndIsEnabledTrue(String tenantId);
    
    Optional<TenantFeature> findByTenantIdAndFeatureCode(String tenantId, String featureCode);
    
    boolean existsByGeneratedId(String generatedId);
    
    boolean existsByTenantIdAndFeatureCode(String tenantId, String featureCode);
    
    List<TenantFeature> findByFeatureCode(String featureCode);
    
    List<TenantFeature> findByTenantIdAndIsSystemFeatureTrue(String tenantId);
}
