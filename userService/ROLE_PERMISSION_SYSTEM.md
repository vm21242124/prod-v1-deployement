# Role and Permission System

This document describes the comprehensive role-based access control (RBAC) system that supports both predefined system roles and tenant-custom roles with flexible permission management.

## Overview

The role and permission system provides:

- **System-wide predefined roles** with standard permissions
- **Tenant-custom roles** that tenants can create and manage
- **Flexible permission assignment** to roles
- **Multiple roles per user** within a tenant
- **Role expiration and assignment tracking**
- **Permission-based access control** throughout the application

## Core Entities

### 1. Permissions
System-wide permissions that define what actions can be performed.

**Key Features:**
- Unique permission codes (e.g., "USER_CREATE", "ROLE_ASSIGN")
- Categorized permissions (User Management, Role Management, etc.)
- Resource and action type classification
- System vs. custom permission support

**Predefined Permission Categories:**
- **User Management**: USER_CREATE, USER_READ, USER_UPDATE, USER_DELETE, USER_MANAGE
- **Role Management**: ROLE_CREATE, ROLE_READ, ROLE_UPDATE, ROLE_DELETE, ROLE_ASSIGN
- **Tenant Management**: TENANT_READ, TENANT_UPDATE, TENANT_MANAGE, TENANT_CONFIGURATION
- **Domain Management**: DOMAIN_CREATE, DOMAIN_READ, DOMAIN_UPDATE, DOMAIN_DELETE
- **Feature Management**: FEATURE_ENABLE, FEATURE_DISABLE, FEATURE_READ
- **Invitation Management**: INVITATION_CREATE, INVITATION_READ, INVITATION_CANCEL
- **Audit and Reports**: AUDIT_READ, REPORT_GENERATE
- **System Administration**: SYSTEM_ADMIN, TENANT_ADMIN

### 2. Roles
Roles that group permissions and can be assigned to users.

**Role Types:**
- **SYSTEM**: System-wide predefined roles (SUPER_ADMIN, SYSTEM_ADMIN, etc.)
- **TENANT_ADMIN**: Tenant administrator role
- **TENANT_USER**: Regular tenant user role
- **CUSTOM**: Tenant-custom roles

**Predefined System Roles:**
- **SUPER_ADMIN**: Full system access
- **SYSTEM_ADMIN**: System administration
- **TENANT_ADMIN**: Tenant administration
- **TENANT_USER**: Regular tenant user
- **GUEST**: Limited access

**Key Features:**
- Tenant-scoped or system-wide
- Priority-based role hierarchy
- Default role assignment
- Active/inactive status

### 3. UserTenantRole
Many-to-many relationship between users and roles within tenants.

**Key Features:**
- Role assignment tracking
- Expiration dates
- Primary role designation
- Assignment history and reasons

## Role Hierarchy and Priority

### System Role Hierarchy (Highest to Lowest Priority)
1. **SUPER_ADMIN** (Priority: 1000)
   - All system permissions
   - Can manage all tenants and users
   - Can create system-wide roles

2. **SYSTEM_ADMIN** (Priority: 900)
   - System administration permissions
   - Can manage tenants and system roles
   - Cannot access tenant-specific data

3. **TENANT_ADMIN** (Priority: 800)
   - Full tenant administration
   - Can manage users, roles, and configurations within tenant
   - Cannot access other tenants

4. **TENANT_USER** (Priority: 100)
   - Basic tenant user permissions
   - Can access tenant resources based on assigned roles

5. **GUEST** (Priority: 50)
   - Limited read-only access
   - No modification permissions

### Tenant Role Priority
- **Custom roles** can have priorities from 1-999
- **Higher priority** roles take precedence in permission conflicts
- **Primary role** designation for user's main role in tenant

## Permission Assignment

### System Permissions
System permissions are predefined and cannot be modified by tenants:

```java
// Example system permissions
Permissions.USER_CREATE
Permissions.ROLE_ASSIGN
Permissions.TENANT_MANAGE
Permissions.SYSTEM_ADMIN
```

### Tenant Custom Permissions
Tenants can create custom permissions for their specific needs:

```java
// Example custom permission
Permissions customPermission = new Permissions();
customPermission.setPermissionCode("CUSTOM_REPORT_GENERATE");
customPermission.setPermissionName("Generate Custom Reports");
customPermission.setResourceType("REPORT");
customPermission.setActionType("GENERATE");
customPermission.setIsSystemPermission(false);
```

## Role Management

### Creating System Roles
```java
// Create a system-wide role
Roles systemRole = new Roles();
systemRole.setRoleCode("SYSTEM_AUDITOR");
systemRole.setRoleName("System Auditor");
systemRole.setRoleType(Roles.RoleType.SYSTEM);
systemRole.setIsSystemRole(true);
systemRole.setPriority(700);
```

### Creating Tenant Custom Roles
```java
// Create a tenant-specific role
Roles customRole = new Roles();
customRole.setTenant(tenant);
customRole.setRoleCode("DEPARTMENT_MANAGER");
customRole.setRoleName("Department Manager");
customRole.setRoleType(Roles.RoleType.CUSTOM);
customRole.setPriority(200);
```

### Assigning Permissions to Roles
```java
// Assign permissions to a role
role.addPermission(userCreatePermission);
role.addPermission(userReadPermission);
role.addPermission(userUpdatePermission);
```

## User Role Assignment

### Single Role Assignment
```java
// Assign a single role to a user
UserTenantRole userRole = new UserTenantRole();
userRole.setUser(user);
userRole.setRole(role);
userRole.setTenant(tenant);
userRole.setIsPrimary(true);
userRole.setAssignedBy("admin");
userRole.setExpiresAt(LocalDateTime.now().plusYears(1));
```

### Multiple Role Assignment
```java
// Assign multiple roles to a user
Set<Long> roleIds = Set.of(adminRoleId, managerRoleId, auditorRoleId);
for (Long roleId : roleIds) {
    UserTenantRole userRole = new UserTenantRole();
    userRole.setUser(user);
    userRole.setRole(roleRepository.findById(roleId).orElse(null));
    userRole.setTenant(tenant);
    userRole.setIsPrimary(roleId.equals(adminRoleId)); // Admin role is primary
    userRole.setAssignedBy("system");
}
```

## Permission Checking

### Role-Based Permission Check
```java
// Check if a role has a specific permission
public boolean hasPermission(Roles role, String permissionCode) {
    return role.getPermissions().stream()
        .anyMatch(permission -> permission.getPermissionCode().equals(permissionCode));
}
```

### User Permission Check
```java
// Check if a user has a specific permission in a tenant
public boolean userHasPermission(User user, Tenant tenant, String permissionCode) {
    return user.getUserTenantRoles().stream()
        .filter(utr -> utr.getTenant().equals(tenant))
        .filter(UserTenantRole::isValid)
        .anyMatch(utr -> utr.getRole().hasPermission(permissionCode));
}
```

### Multiple Role Permission Check
```java
// Check permissions across multiple roles (highest priority wins)
public boolean userHasPermissionWithPriority(User user, Tenant tenant, String permissionCode) {
    return user.getUserTenantRoles().stream()
        .filter(utr -> utr.getTenant().equals(tenant))
        .filter(UserTenantRole::isValid)
        .sorted((utr1, utr2) -> Integer.compare(
            utr2.getRole().getPriority(), 
            utr1.getRole().getPriority()
        ))
        .anyMatch(utr -> utr.getRole().hasPermission(permissionCode));
}
```

## Database Schema

### Tables
1. **permissions** - System-wide permissions
2. **roles** - Roles (system-wide and tenant-specific)
3. **role_permissions** - Many-to-many relationship between roles and permissions
4. **user_tenant_roles** - User-role assignments within tenants

### Key Relationships
- `roles.tenant_id` - Links roles to tenants (null for system roles)
- `user_tenant_roles` - Links users to roles within specific tenants
- `role_permissions` - Links roles to permissions

## Security Considerations

### 1. Permission Inheritance
- Higher priority roles override lower priority permissions
- System roles cannot be modified by tenants
- Tenant roles are isolated to their respective tenants

### 2. Role Assignment Security
- Only users with ROLE_ASSIGN permission can assign roles
- Role assignments are tracked with audit logs
- Expired roles are automatically deactivated

### 3. Permission Validation
- All API endpoints should validate user permissions
- Permission checks should be performed at the service layer
- Failed permission checks should be logged for security monitoring

### 4. Data Isolation
- Tenant roles cannot access other tenant data
- System roles have appropriate data access restrictions
- User role assignments are tenant-scoped

## Usage Examples

### Creating a Tenant with Default Roles
```java
// When creating a new tenant, automatically create default roles
public void createTenantWithDefaultRoles(CreateTenantRequest request) {
    Tenant tenant = createTenant(request);
    
    // Create tenant admin role
    Roles tenantAdminRole = createTenantAdminRole(tenant);
    
    // Create default user role
    Roles defaultUserRole = createDefaultUserRole(tenant);
    
    // Assign admin role to the first user
    assignRoleToUser(request.getAdminUserId(), tenantAdminRole, true);
}
```

### Managing User Roles
```java
// Assign multiple roles to a user
public void assignRolesToUser(Long userId, Set<Long> roleIds, Long tenantId) {
    User user = userRepository.findById(userId).orElseThrow();
    Tenant tenant = tenantRepository.findById(tenantId).orElseThrow();
    
    for (Long roleId : roleIds) {
        Roles role = roleRepository.findById(roleId).orElseThrow();
        UserTenantRole userRole = new UserTenantRole();
        userRole.setUser(user);
        userRole.setRole(role);
        userRole.setTenant(tenant);
        userRole.setAssignedBy(getCurrentUser().getUsername());
        userTenantRoleRepository.save(userRole);
    }
}
```

### Permission-Based Access Control
```java
// Service method with permission check
@PreAuthorize("hasPermission(#tenantId, 'USER_CREATE')")
public UserDto createUser(CreateUserRequest request, Long tenantId) {
    // Business logic for creating user
    return userService.createUser(request, tenantId);
}
```

## Best Practices

### 1. Role Design
- Keep roles focused and specific
- Use descriptive role names
- Document role purposes and permissions
- Regularly review and update roles

### 2. Permission Management
- Use consistent permission naming conventions
- Group related permissions into categories
- Avoid overly granular permissions
- Document permission purposes

### 3. User Role Assignment
- Assign minimal necessary roles
- Use role expiration for temporary access
- Regularly review user role assignments
- Implement role approval workflows for sensitive roles

### 4. Security Monitoring
- Log all role and permission changes
- Monitor failed permission checks
- Implement role assignment approval for sensitive roles
- Regular security audits of role assignments

## Next Steps

1. Implement repository interfaces for all entities
2. Create service layer with business logic
3. Implement permission checking utilities
4. Add role and permission management controllers
5. Implement security interceptors for permission validation
6. Create database migration scripts
7. Add comprehensive unit and integration tests
8. Implement role assignment workflows
9. Add audit logging for role and permission changes
