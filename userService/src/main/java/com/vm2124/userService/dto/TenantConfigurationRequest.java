package com.vm2124.userService.dto;

import com.vm2124.userService.model.TenantConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantConfigurationRequest {
    
    private String configKey;
    private String configValue;
    private TenantConfiguration.ConfigType configType;
    private Boolean isEncrypted;
    private Boolean isRequired;
    private String description;
}
