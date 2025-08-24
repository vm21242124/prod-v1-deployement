# Onboard Data API

This document describes the OnboardDataController API that allows you to initialize the system with predefined modules, permissions, roles, and features.

## API Endpoints

### 1. Initialize All System Data

```
POST /api/v1/onboard-data/initialize
```

Initializes all predefined system data including modules, permissions, roles, and features.

### 2. Onboard Specific Data Types

```
POST /api/v1/onboard-data/modules
POST /api/v1/onboard-data/permissions
POST /api/v1/onboard-data/roles
POST /api/v1/onboard-data/features
```

Onboards specific types of data.

### 3. Get Onboard Status

```
GET /api/v1/onboard-data/status
```

Retrieves the current status of onboarded data.

## Request Body

```json
{
  "onboardModules": true,
  "onboardPermissions": true,
  "onboardRoles": true,
  "onboardFeatures": true,
  "specificModules": ["USER_MANAGEMENT", "ROLE_MANAGEMENT"],
  "specificPermissions": ["USER_CREATE", "USER_READ"],
  "specificRoles": ["SUPER_ADMIN", "TENANT_ADMIN"],
  "specificFeatures": ["SECURITY", "BACKUP_RESTORE"],
  "forceRecreate": false,
  "tenantId": "TNT1234567"
}
```

### Request Parameters

- `onboardModules`: Whether to onboard modules (default: true)
- `onboardPermissions`: Whether to onboard permissions (default: true)
- `onboardRoles`: Whether to onboard roles (default: true)
- `onboardFeatures`: Whether to onboard features (default: true)
- `specificModules`: Specific modules to onboard (if empty, onboard all predefined modules)
- `specificPermissions`: Specific permissions to onboard (if empty, onboard all predefined permissions)
- `specificRoles`: Specific roles to onboard (if empty, onboard all predefined roles)
- `specificFeatures`: Specific features to onboard (if empty, onboard all predefined features)
- `forceRecreate`: Force recreation of existing data (default: false)
- `tenantId`: Tenant ID for tenant-specific data (optional)

## Response

### Success Response (201 Created)

```json
{
  "success": true,
  "message": "System data initialized successfully. Total items created: 25",
  "timestamp": "2024-01-15T10:30:00",
  "modulesCreated": 5,
  "permissionsCreated": 10,
  "rolesCreated": 5,
  "featuresCreated": 5,
  "createdModules": [
    "USER_MANAGEMENT",
    "ROLE_MANAGEMENT",
    "TENANT_CONFIGURATION",
    "AUDIT_LOGS",
    "REPORTING"
  ],
  "createdPermissions": [
    "USER_CREATE",
    "USER_READ",
    "USER_UPDATE",
    "USER_DELETE",
    "ROLE_CREATE",
    "ROLE_READ",
    "ROLE_UPDATE",
    "ROLE_DELETE",
    "TENANT_CONFIG_READ",
    "TENANT_CONFIG_UPDATE"
  ],
  "createdRoles": [
    "SUPER_ADMIN",
    "SYSTEM_ADMIN",
    "TENANT_ADMIN",
    "TENANT_USER",
    "GUEST"
  ],
  "createdFeatures": [
    "SECURITY",
    "BACKUP_RESTORE",
    "API_MANAGEMENT",
    "WORKFLOW",
    "DOCUMENT_MANAGEMENT"
  ],
  "status": null,
  "errorDetails": null
}
```

### Error Response (400 Bad Request)

```json
{
  "success": false,
  "message": "Database connection failed",
  "timestamp": "2024-01-15T10:30:00",
  "modulesCreated": 0,
  "permissionsCreated": 0,
  "rolesCreated": 0,
  "featuresCreated": 0,
  "createdModules": null,
  "createdPermissions": null,
  "createdRoles": null,
  "createdFeatures": null,
  "status": null,
  "errorDetails": "Connection timeout"
}
```

## Predefined Data

### Modules

The system includes the following predefined modules:

- `USER_MANAGEMENT`: User Management
- `ROLE_MANAGEMENT`: Role Management
- `TENANT_CONFIGURATION`: Tenant Configuration
- `AUDIT_LOGS`: Audit Logs
- `REPORTING`: Reporting
- `ANALYTICS`: Analytics
- `NOTIFICATIONS`: Notifications
- `INTEGRATIONS`: Integrations
- `SECURITY`: Security
- `BACKUP_RESTORE`: Backup & Restore
- `API_MANAGEMENT`: API Management
- `WORKFLOW`: Workflow Management
- `DOCUMENT_MANAGEMENT`: Document Management
- `COMMUNICATION`: Communication Tools
- `CALENDAR`: Calendar Management
- `TASK_MANAGEMENT`: Task Management

### Permissions

The system includes the following predefined permissions:

#### User Permissions
- `USER_CREATE`: Create users
- `USER_READ`: Read user information
- `USER_UPDATE`: Update user information
- `USER_DELETE`: Delete users
- `USER_ACTIVATE`: Activate users
- `USER_DEACTIVATE`: Deactivate users
- `USER_RESET_PASSWORD`: Reset user passwords

#### Role Permissions
- `ROLE_CREATE`: Create roles
- `ROLE_READ`: Read role information
- `ROLE_UPDATE`: Update role information
- `ROLE_DELETE`: Delete roles
- `ROLE_ASSIGN`: Assign roles to users
- `ROLE_REMOVE`: Remove roles from users

#### Tenant Permissions
- `TENANT_CONFIG_READ`: Read tenant configuration
- `TENANT_CONFIG_UPDATE`: Update tenant configuration
- `TENANT_CREATE`: Create tenants
- `TENANT_READ`: Read tenant information
- `TENANT_UPDATE`: Update tenant information
- `TENANT_DELETE`: Delete tenants

#### Feature Permissions
- `FEATURE_ENABLE`: Enable features
- `FEATURE_DISABLE`: Disable features
- `FEATURE_READ`: Read feature information
- `FEATURE_UPDATE`: Update feature configuration

#### Audit Permissions
- `AUDIT_READ`: Read audit logs
- `AUDIT_EXPORT`: Export audit logs
- `AUDIT_DELETE`: Delete audit logs

#### Reporting Permissions
- `REPORT_CREATE`: Create reports
- `REPORT_READ`: Read reports
- `REPORT_UPDATE`: Update reports
- `REPORT_DELETE`: Delete reports
- `REPORT_EXPORT`: Export reports

#### System Permissions
- `SYSTEM_CONFIG_READ`: Read system configuration
- `SYSTEM_CONFIG_UPDATE`: Update system configuration
- `SYSTEM_BACKUP`: Create system backups
- `SYSTEM_RESTORE`: Restore system from backup

### Roles

The system includes the following predefined roles:

#### SUPER_ADMIN
- **Description**: System-wide super administrator with all permissions
- **Priority**: 1000
- **Permissions**: All permissions

#### SYSTEM_ADMIN
- **Description**: System administrator with most permissions
- **Priority**: 900
- **Permissions**: User management, role management, tenant configuration, feature management, audit, reporting, system configuration

#### TENANT_ADMIN
- **Description**: Tenant administrator with tenant-specific permissions
- **Priority**: 800
- **Permissions**: User management, role management, tenant configuration, feature management, audit, reporting

#### TENANT_USER
- **Description**: Regular tenant user with basic permissions
- **Priority**: 100
- **Permissions**: Read access to users, roles, tenant configuration, features, reports

#### GUEST
- **Description**: Guest user with minimal permissions
- **Priority**: 10
- **Permissions**: Read access to users and features

### Features

The system includes the following predefined features:

- `USER_MANAGEMENT`: Complete user management functionality
- `ROLE_MANAGEMENT`: Role and permission management
- `TENANT_CONFIGURATION`: Tenant configuration management
- `AUDIT_LOGS`: Comprehensive audit logging
- `REPORTING`: Advanced reporting capabilities
- `ANALYTICS`: Data analytics and insights
- `NOTIFICATIONS`: Email and push notifications
- `INTEGRATIONS`: Third-party integrations
- `SECURITY`: Advanced security features
- `BACKUP_RESTORE`: Data backup and restore
- `API_MANAGEMENT`: API management and monitoring
- `WORKFLOW`: Workflow automation
- `DOCUMENT_MANAGEMENT`: Document storage and management
- `COMMUNICATION`: Internal communication tools
- `CALENDAR`: Calendar and scheduling
- `TASK_MANAGEMENT`: Task tracking and management

## Example Usage

### Initialize All System Data

```bash
curl -X POST http://localhost:8080/api/v1/onboard-data/initialize \
  -H "Content-Type: application/json" \
  -d '{
    "onboardModules": true,
    "onboardPermissions": true,
    "onboardRoles": true,
    "onboardFeatures": true
  }'
```

### Onboard Specific Modules

```bash
curl -X POST http://localhost:8080/api/v1/onboard-data/modules \
  -H "Content-Type: application/json" \
  -d '{
    "specificModules": ["USER_MANAGEMENT", "ROLE_MANAGEMENT", "SECURITY"]
  }'
```

### Onboard Specific Roles

```bash
curl -X POST http://localhost:8080/api/v1/onboard-data/roles \
  -H "Content-Type: application/json" \
  -d '{
    "specificRoles": ["SUPER_ADMIN", "TENANT_ADMIN"]
  }'
```

### Get Onboard Status

```bash
curl -X GET http://localhost:8080/api/v1/onboard-data/status
```

## JavaScript Examples

### Initialize System Data

```javascript
const response = await fetch('/api/v1/onboard-data/initialize', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    onboardModules: true,
    onboardPermissions: true,
    onboardRoles: true,
    onboardFeatures: true
  })
});

const result = await response.json();
console.log('Created:', result.modulesCreated + result.rolesCreated + result.permissionsCreated + result.featuresCreated, 'items');
```

### Onboard Specific Data

```javascript
const response = await fetch('/api/v1/onboard-data/modules', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    specificModules: ['USER_MANAGEMENT', 'SECURITY', 'BACKUP_RESTORE']
  })
});

const result = await response.json();
console.log('Created modules:', result.createdModules);
```

## Status Endpoint Response

The status endpoint returns information about existing onboarded data:

```json
{
  "success": true,
  "message": "Onboard status retrieved successfully",
  "timestamp": "2024-01-15T10:30:00",
  "status": {
    "systemRolesCount": 5,
    "systemFeaturesCount": 15,
    "existingRoles": [
      "SUPER_ADMIN",
      "SYSTEM_ADMIN",
      "TENANT_ADMIN",
      "TENANT_USER",
      "GUEST"
    ],
    "existingFeatures": [
      "USER_MANAGEMENT",
      "ROLE_MANAGEMENT",
      "TENANT_CONFIGURATION",
      "AUDIT_LOGS",
      "REPORTING",
      "SECURITY",
      "BACKUP_RESTORE"
    ]
  }
}
```

## Error Handling

The API will return appropriate error messages for:

- Database connection failures
- Invalid data formats
- Missing required fields
- Duplicate data creation attempts
- Permission issues

## Security Considerations

- All operations are logged for audit purposes
- System roles and features are protected from unauthorized modification
- Generated IDs are used for all entities
- Transaction safety ensures data consistency
- Duplicate prevention mechanisms are in place
