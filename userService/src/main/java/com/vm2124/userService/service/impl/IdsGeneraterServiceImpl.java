package com.vm2124.userService.service.impl;

import com.vm2124.userService.service.IdsGeneraterService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class IdsGeneraterServiceImpl implements IdsGeneraterService {
    
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int ID_LENGTH = 10;
    private final SecureRandom random = new SecureRandom();
    private final AtomicLong counter = new AtomicLong(1);
    
    @Override
    public String generateTenantId() {
        return generateId("TNT");
    }
    
    @Override
    public String generateUserId() {
        return generateId("USR");
    }
    
    @Override
    public String generateRoleId() {
        return generateId("ROL");
    }
    
    @Override
    public String generateFeatureId() {
        return generateId("FEA");
    }
    
    @Override
    public String generateTenantRoleId() {
        return generateId("TNR");
    }
    
    @Override
    public String generateTenantFeatureId() {
        return generateId("TNF");
    }
    
    @Override
    public String generateUserTenantRoleId() {
        return generateId("UTR");
    }
    
    private String generateId(String prefix) {
        StringBuilder id = new StringBuilder(prefix);
        
        // Generate random characters for the remaining length
        for (int i = 0; i < ID_LENGTH - prefix.length(); i++) {
            id.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        
        return id.toString();
    }
    
    /**
     * Alternative method that includes a counter for better uniqueness
     */
    private String generateIdWithCounter(String prefix) {
        StringBuilder id = new StringBuilder(prefix);
        long currentCounter = counter.getAndIncrement();
        
        // Convert counter to base36 and pad with zeros
        String counterStr = Long.toString(currentCounter, 36).toUpperCase();
        int remainingLength = ID_LENGTH - prefix.length();
        
        // Pad with zeros if needed
        while (counterStr.length() < remainingLength) {
            counterStr = "0" + counterStr;
        }
        
        // Truncate if too long
        if (counterStr.length() > remainingLength) {
            counterStr = counterStr.substring(counterStr.length() - remainingLength);
        }
        
        id.append(counterStr);
        return id.toString();
    }
}
