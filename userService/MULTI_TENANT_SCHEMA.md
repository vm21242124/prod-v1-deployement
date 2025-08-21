# Multi-Tenant User Management Schema

This document describes the comprehensive multi-tenant user management system where each tenant can have different domains, configurations, and user management capabilities.

## Overview

The multi-tenant system allows multiple organizations (tenants) to use the same application while maintaining complete data isolation and customization capabilities. Each tenant can have:

- Multiple domains and subdomains
- Custom branding and configuration
- Tenant-specific roles and permissions
- Feature flags and capabilities
- Audit logging and compliance
- User invitations and management

## Core Entities

### 1. Tenant
The main entity representing an organization or customer.

**Key Features:**
- Unique tenant code and domain
- Subscription plan management
- Custom branding (logo, colors)
- User limits and quotas
- Timezone and locale settings
- Status management (active, inactive, suspended)

**Fields:**
- `id`: Primary key
- `tenantCode`: Unique identifier (e.g., "acme-corp")
- `name`: Display name
- `domain`: Primary domain
- `subscriptionPlan`: BASIC, PREMIUM, ENTERPRISE, CUSTOM
- `maxUsers`: Maximum number of users allowed
- `customDomain`: Custom domain name
- `logoUrl`, `primaryColor`, `secondaryColor`: Branding
- `timezone`, `locale`: Regional settings

### 2. User
Enhanced user entity with multi-tenant support.

**Key Features:**
- Belongs to a specific tenant
- Enhanced user profile (employee ID, department, job title)
- Security features (failed login attempts, account locking)
- Manager hierarchy support
- Last login tracking

**Fields:**
- `tenant`: Reference to tenant
- `username`, `email`, `firstName`, `lastName`
- `employeeId`, `department`, `jobTitle`
- `managerId`: Hierarchical management
- `lastLoginAt`, `passwordChangedAt`
- `failedLoginAttempts`, `accountLockedUntil`

### 3. TenantRole
Tenant-specific roles with custom permissions.

**Key Features:**
- Tenant-scoped roles
- Custom permission sets
- System vs. custom roles
- Default role assignment

**Fields:**
- `tenant`: Reference to tenant
- `name`, `description`
- `isSystemRole`: Built-in vs. custom
- `isDefault`: Default role for new users
- `permissions`: Set of permission strings

### 4. UserRole
Many-to-many relationship between users and roles.

**Key Features:**
- Role assignment tracking
- Expiration dates
- Assignment history

**Fields:**
- `user`, `role`: References
- `assignedBy`, `assignedAt`
- `expiresAt`, `isActive`

### 5. TenantConfiguration
Flexible configuration storage for tenants.

**Key Features:**
- Key-value configuration pairs
- Multiple data types (string, integer, boolean, JSON)
- Encrypted configuration support
- Required vs. optional settings

**Fields:**
- `tenant`: Reference to tenant
- `configKey`, `configValue`
- `configType`: STRING, INTEGER, BOOLEAN, JSON, ENCRYPTED
- `isEncrypted`, `isRequired`

### 6. TenantDomain
Multiple domain management per tenant.

**Key Features:**
- Primary and custom domains
- Domain verification
- SSL certificate management
- Subdomain support

**Fields:**
- `tenant`: Reference to tenant
- `domain`: Domain name
- `domainType`: PRIMARY, CUSTOM, SUBDOMAIN, ALIAS
- `isPrimary`, `isVerified`
- `sslCertificate`, `sslPrivateKey`

### 7. TenantInvitation
User invitation management.

**Key Features:**
- Email-based invitations
- Role assignment during invitation
- Expiration management
- Invitation tracking

**Fields:**
- `tenant`: Reference to tenant
- `email`, `firstName`, `lastName`
- `invitationToken`: Secure token
- `role`: Assigned role
- `status`: PENDING, ACCEPTED, EXPIRED, CANCELLED
- `expiresAt`, `acceptedAt`

### 8. TenantAuditLog
Comprehensive audit logging.

**Key Features:**
- Tenant-scoped audit trails
- User action tracking
- Resource-level auditing
- Security event logging

**Fields:**
- `tenant`, `user`: References
- `action`: Performed action
- `resourceType`, `resourceId`, `resourceName`
- `ipAddress`, `userAgent`, `sessionId`
- `severity`: INFO, WARNING, ERROR, CRITICAL

### 9. TenantFeature
Feature flag management.

**Key Features:**
- Tenant-specific feature toggles
- Multiple data types for feature values
- System vs. custom features

**Fields:**
- `tenant`: Reference to tenant
- `featureCode`, `featureName`
- `isEnabled`: Feature toggle
- `configValue`: Feature configuration
- `featureType`: BOOLEAN, STRING, INTEGER, JSON, ENCRYPTED

## Data Transfer Objects (DTOs)

### TenantDto
Complete tenant information including statistics.

### UserDto
Enhanced user information with tenant context and permissions.

### TenantRoleDto
Role information with user count and permissions.

### TenantInvitationDto
Invitation details with computed fields (expiration, invitation URL).

### CreateTenantRequest
Comprehensive tenant creation request including:
- Tenant details
- Admin user creation
- Initial domains
- Feature enablement
- Configuration setup

## Key Features

### 1. Domain Management
- Multiple domains per tenant
- Custom domain support
- SSL certificate management
- Domain verification workflow

### 2. Role-Based Access Control (RBAC)
- Tenant-scoped roles
- Custom permission sets
- Hierarchical permissions
- Role expiration and assignment tracking

### 3. User Management
- Tenant-specific user creation
- User invitation workflow
- Manager hierarchy
- Security features (account locking, failed login tracking)

### 4. Configuration Management
- Flexible key-value storage
- Multiple data types
- Encrypted configuration support
- Required vs. optional settings

### 5. Feature Management
- Feature flags per tenant
- Subscription-based feature access
- Custom feature configuration

### 6. Audit and Compliance
- Comprehensive audit logging
- Tenant-scoped audit trails
- Security event tracking
- Compliance reporting

### 7. Branding and Customization
- Custom logos and colors
- Tenant-specific themes
- Localization support (timezone, locale)

## Database Schema

The system uses the following database tables:

1. `tenants` - Main tenant information
2. `users` - User accounts with tenant reference
3. `tenant_roles` - Tenant-specific roles
4. `user_roles` - User-role assignments
5. `tenant_configurations` - Tenant configuration storage
6. `tenant_domains` - Domain management
7. `tenant_invitations` - User invitations
8. `tenant_audit_logs` - Audit trail
9. `tenant_features` - Feature flags
10. `tenant_role_permissions` - Role permissions (element collection)

## Security Considerations

1. **Data Isolation**: All entities are tenant-scoped
2. **Encrypted Configuration**: Sensitive configuration can be encrypted
3. **Audit Logging**: All actions are logged for compliance
4. **Account Security**: Failed login tracking and account locking
5. **Invitation Security**: Secure tokens for user invitations
6. **Role-Based Access**: Fine-grained permission control

## Usage Examples

### Creating a New Tenant
```java
CreateTenantRequest request = new CreateTenantRequest();
request.setTenantCode("acme-corp");
request.setName("Acme Corporation");
request.setDomain("acme.example.com");
request.setSubscriptionPlan(SubscriptionPlan.PREMIUM);
request.setMaxUsers(500);
// ... set other fields
```

### Managing User Roles
```java
// Assign role to user
UserRole userRole = new UserRole();
userRole.setUser(user);
userRole.setRole(adminRole);
userRole.setAssignedBy("system");
userRole.setExpiresAt(LocalDateTime.now().plusYears(1));
```

### Feature Management
```java
// Enable feature for tenant
TenantFeature feature = new TenantFeature();
feature.setTenant(tenant);
feature.setFeatureCode("advanced_analytics");
feature.setFeatureName("Advanced Analytics");
feature.setIsEnabled(true);
feature.setConfigValue("{\"retention_days\": 90}");
```

## Next Steps

1. Implement repository interfaces for all entities
2. Create service layer with business logic
3. Implement controllers for REST API
4. Add validation and error handling
5. Implement security and authentication
6. Create database migration scripts
7. Add unit and integration tests
8. Implement monitoring and logging
