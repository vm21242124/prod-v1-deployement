package com.vm2124.userService.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleData {
    private String code;
    private String name;
    private String description;
    private int priority;
    private boolean isSystemRole;
    private boolean isDefault;
    private String roleType;
    private List<String> permissions;
}
