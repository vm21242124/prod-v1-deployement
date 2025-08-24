package com.vm2124.userService.service;

public interface IdsGeneraterService {
    String generateTenantId();
    String generateUserId();
    String generateRoleId();
    String generateFeatureId();
    String generateTenantRoleId();
    String generateTenantFeatureId();
    String generateUserTenantRoleId();
}
