# UUID-Based Reference System

This document describes the updated multi-tenant user management system that uses UUID primary keys and string references instead of JPA mappings.

## Overview

The system has been updated to use:
- **UUID primary keys** for all entities
- **String references** instead of JPA relationships
- **Simplified data model** without complex mappings
- **Better performance** and flexibility

## Key Changes

### 1. Primary Keys
All entities now use UUID as primary keys:
```java
@Id
@GeneratedValue(strategy = GenerationType.UUID)
private UUID id;
```

### 2. String References
Instead of JPA relationships, entities use string references to UUIDs:
```java
// Before (JPA mapping)
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "tenant_id", nullable = false)
private Tenant tenant;

// After (String reference)
@Column(name = "tenant_id", nullable = false)
private String tenantId; // Reference to tenant UUID as string
```

### 3. Simplified Relationships
- No more complex JPA mappings
- No lazy loading issues
- Better performance for large datasets
- Easier to work with in distributed systems

## Entity Structure

### User Entity
```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "tenant_id", nullable = false)
    private String tenantId; // Reference to tenant UUID
    
    @Column(name = "manager_id")
    private String managerId; // Reference to another user UUID
    
    // Other fields...
}
```

### Tenant Entity
```java
@Entity
@Table(name = "tenants")
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false, unique = true)
    private String tenantCode;
    
    // Other fields...
}
```

### Roles Entity
```java
@Entity
@Table(name = "roles")
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "tenant_id")
    private String tenantId; // null for system-wide roles
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "role_permissions", 
                     joinColumns = @JoinColumn(name = "role_id"))
    @Column(name = "permission_code")
    private Set<String> permissionCodes = new HashSet<>();
    
    // Other fields...
}
```

### UserTenantRole Entity
```java
@Entity
@Table(name = "user_tenant_roles")
public class UserTenantRole {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "user_id", nullable = false)
    private String userId; // Reference to user UUID
    
    @Column(name = "role_id", nullable = false)
    private String roleId; // Reference to role UUID
    
    @Column(name = "tenant_id", nullable = false)
    private String tenantId; // Reference to tenant UUID
    
    // Other fields...
}
```

## DTO Updates

### UserDto
```java
public class UserDto {
    private UUID id;
    private String tenantId; // String reference
    private String managerId; // String reference
    private Set<String> roles; // Role codes
    private Set<String> permissions; // Permission codes
    // Other fields...
}
```

### RoleDto
```java
public class RoleDto {
    private UUID id;
    private String tenantId; // String reference
    private Set<String> permissions; // Permission codes
    // Other fields...
}
```

## Benefits of This Approach

### 1. Performance
- **No lazy loading issues** - all data is loaded explicitly
- **Faster queries** - no complex joins
- **Better caching** - simpler object graphs
- **Reduced memory usage** - no proxy objects

### 2. Flexibility
- **Easier to work with** in service layer
- **Better for distributed systems** - no entity state issues
- **Simpler serialization** - no circular reference problems
- **Easier testing** - no complex setup required

### 3. Scalability
- **Better for microservices** - no shared entity state
- **Easier to partition** data across databases
- **Better for event sourcing** - simple string references
- **Easier to migrate** between different storage systems

### 4. Maintainability
- **Simpler code** - no complex JPA mappings
- **Easier to understand** - explicit relationships
- **Better debugging** - clear data flow
- **Easier to refactor** - loose coupling

## Data Access Patterns

### 1. Finding Related Data
```java
// Find user's tenant
public Tenant getUserTenant(String userId) {
    User user = userRepository.findById(userId).orElse(null);
    if (user != null) {
        return tenantRepository.findById(user.getTenantId()).orElse(null);
    }
    return null;
}

// Find user's roles
public List<Roles> getUserRoles(String userId) {
    List<UserTenantRole> userRoles = userTenantRoleRepository.findByUserId(userId);
    List<String> roleIds = userRoles.stream()
        .map(UserTenantRole::getRoleId)
        .collect(Collectors.toList());
    return roleRepository.findAllById(roleIds);
}
```

### 2. Permission Checking
```java
public boolean userHasPermission(String userId, String tenantId, String permissionCode) {
    // Get user's roles in the tenant
    List<UserTenantRole> userRoles = userTenantRoleRepository
        .findByUserIdAndTenantId(userId, tenantId);
    
    // Get role IDs
    List<String> roleIds = userRoles.stream()
        .filter(UserTenantRole::isValid)
        .map(UserTenantRole::getRoleId)
        .collect(Collectors.toList());
    
    // Check if any role has the permission
    return roleRepository.findAllById(roleIds).stream()
        .anyMatch(role -> role.hasPermission(permissionCode));
}
```

### 3. Creating Relationships
```java
public void assignRoleToUser(String userId, String roleId, String tenantId) {
    UserTenantRole userRole = new UserTenantRole();
    userRole.setUserId(userId);
    userRole.setRoleId(roleId);
    userRole.setTenantId(tenantId);
    userRole.setAssignedBy(getCurrentUser().getId().toString());
    userTenantRoleRepository.save(userRole);
}
```

## Database Schema

### Tables with UUID Primary Keys
1. **users** - `id` (UUID), `tenant_id` (VARCHAR), `manager_id` (VARCHAR)
2. **tenants** - `id` (UUID), `tenant_code` (VARCHAR)
3. **roles** - `id` (UUID), `tenant_id` (VARCHAR)
4. **permissions** - `id` (UUID), `permission_code` (VARCHAR)
5. **user_tenant_roles** - `id` (UUID), `user_id` (VARCHAR), `role_id` (VARCHAR), `tenant_id` (VARCHAR)
6. **tenant_configurations** - `id` (UUID), `tenant_id` (VARCHAR)
7. **tenant_domains** - `id` (UUID), `tenant_id` (VARCHAR)
8. **tenant_invitations** - `id` (UUID), `tenant_id` (VARCHAR), `invited_by` (VARCHAR), `role_id` (VARCHAR)
9. **tenant_audit_logs** - `id` (UUID), `tenant_id` (VARCHAR), `user_id` (VARCHAR)
10. **tenant_features** - `id` (UUID), `tenant_id` (VARCHAR)

### Reference Columns
All reference columns are VARCHAR fields containing UUID strings:
- `tenant_id` - References tenant UUID
- `user_id` - References user UUID
- `role_id` - References role UUID
- `manager_id` - References user UUID
- `invited_by` - References user UUID
- `accepted_by` - References user UUID

## Migration Considerations

### 1. Database Migration
```sql
-- Example migration for users table
ALTER TABLE users 
ADD COLUMN id_new UUID DEFAULT gen_random_uuid(),
ADD COLUMN tenant_id_new VARCHAR(36),
ADD COLUMN manager_id_new VARCHAR(36);

-- Update references
UPDATE users SET 
tenant_id_new = (SELECT id::text FROM tenants WHERE tenants.id = users.tenant_id_old),
manager_id_new = (SELECT id::text FROM users u2 WHERE u2.id = users.manager_id_old);

-- Drop old columns and rename new ones
ALTER TABLE users DROP COLUMN id, DROP COLUMN tenant_id, DROP COLUMN manager_id;
ALTER TABLE users RENAME COLUMN id_new TO id;
ALTER TABLE users RENAME COLUMN tenant_id_new TO tenant_id;
ALTER TABLE users RENAME COLUMN manager_id_new TO manager_id;
```

### 2. Application Migration
```java
// Update repository methods
public interface UserRepository extends JpaRepository<User, UUID> {
    List<User> findByTenantId(String tenantId);
    Optional<User> findByUsernameAndTenantId(String username, String tenantId);
}

// Update service methods
public UserDto getUserById(String userId) {
    UUID userUuid = UUID.fromString(userId);
    User user = userRepository.findById(userUuid).orElse(null);
    return convertToDto(user);
}
```

## Best Practices

### 1. UUID Handling
```java
// Convert between String and UUID
public UUID stringToUuid(String uuidString) {
    return UUID.fromString(uuidString);
}

public String uuidToString(UUID uuid) {
    return uuid.toString();
}

// Validate UUID format
public boolean isValidUuid(String uuidString) {
    try {
        UUID.fromString(uuidString);
        return true;
    } catch (IllegalArgumentException e) {
        return false;
    }
}
```

### 2. Reference Validation
```java
// Validate references before saving
public void validateReferences(User user) {
    if (user.getTenantId() != null) {
        if (!tenantRepository.existsById(UUID.fromString(user.getTenantId()))) {
            throw new IllegalArgumentException("Invalid tenant reference");
        }
    }
    
    if (user.getManagerId() != null) {
        if (!userRepository.existsById(UUID.fromString(user.getManagerId()))) {
            throw new IllegalArgumentException("Invalid manager reference");
        }
    }
}
```

### 3. Batch Operations
```java
// Efficient batch loading
public List<User> getUsersWithTenants(List<String> userIds) {
    List<UUID> uuids = userIds.stream()
        .map(UUID::fromString)
        .collect(Collectors.toList());
    
    List<User> users = userRepository.findAllById(uuids);
    
    // Load tenants in batch
    Set<String> tenantIds = users.stream()
        .map(User::getTenantId)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());
    
    Map<String, Tenant> tenants = tenantRepository.findAllById(
        tenantIds.stream().map(UUID::fromString).collect(Collectors.toList())
    ).stream().collect(Collectors.toMap(
        tenant -> tenant.getId().toString(),
        tenant -> tenant
    ));
    
    return users;
}
```

## Security Considerations

### 1. UUID Validation
- Always validate UUID format before processing
- Use parameterized queries to prevent injection
- Log invalid UUID attempts for security monitoring

### 2. Reference Integrity
- Validate all references before saving
- Use database constraints where possible
- Implement soft deletes to maintain referential integrity

### 3. Access Control
- Validate tenant access for all operations
- Use UUID-based session tokens
- Implement proper audit logging for all operations

## Performance Optimization

### 1. Indexing
```sql
-- Create indexes for reference columns
CREATE INDEX idx_users_tenant_id ON users(tenant_id);
CREATE INDEX idx_users_manager_id ON users(manager_id);
CREATE INDEX idx_user_tenant_roles_user_id ON user_tenant_roles(user_id);
CREATE INDEX idx_user_tenant_roles_tenant_id ON user_tenant_roles(tenant_id);
CREATE INDEX idx_roles_tenant_id ON roles(tenant_id);
```

### 2. Caching
```java
// Cache frequently accessed data
@Cacheable("tenants")
public Tenant getTenantById(String tenantId) {
    return tenantRepository.findById(UUID.fromString(tenantId)).orElse(null);
}

@Cacheable("users")
public User getUserById(String userId) {
    return userRepository.findById(UUID.fromString(userId)).orElse(null);
}
```

### 3. Batch Processing
```java
// Process data in batches
public void assignRolesToUsers(Map<String, List<String>> userRoleAssignments) {
    List<UserTenantRole> assignments = new ArrayList<>();
    
    userRoleAssignments.forEach((userId, roleIds) -> {
        roleIds.forEach(roleId -> {
            UserTenantRole assignment = new UserTenantRole();
            assignment.setUserId(userId);
            assignment.setRoleId(roleId);
            assignments.add(assignment);
        });
    });
    
    userTenantRoleRepository.saveAll(assignments);
}
```

This UUID-based reference system provides a more flexible, performant, and maintainable approach to the multi-tenant user management system.
