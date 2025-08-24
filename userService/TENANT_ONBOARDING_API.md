# Tenant Onboarding API

This document describes the tenant onboarding API that allows you to create a new tenant with modules and an admin user.

## API Endpoint

```
POST /api/v1/tenants/onboard
```

## Request Body

```json
{
  "tenantCode": "COMPANY001",
  "name": "Acme Corporation",
  "domain": "acme.com",
  "description": "Acme Corporation tenant",
  "subscriptionPlan": "BASIC",
  "maxUsers": 100,
  "customDomain": "app.acme.com",
  "logoUrl": "https://example.com/logo.png",
  "primaryColor": "#007bff",
  "secondaryColor": "#6c757d",
  "timezone": "UTC",
  "locale": "en_US",
  "adminUsername": "admin",
  "adminEmail": "admin@acme.com",
  "adminFirstName": "John",
  "adminLastName": "Doe",
  "adminPassword": "securePassword123",
  "adminPhoneNumber": "1234567890",
  "adminDepartment": "IT",
  "adminJobTitle": "System Administrator",
  "enabledModules": [
    "USER_MANAGEMENT",
    "ROLE_MANAGEMENT",
    "TENANT_CONFIGURATION",
    "AUDIT_LOGS",
    "REPORTING"
  ],
  "configurations": []
}
```

### Required Fields

- `tenantCode`: Unique identifier for the tenant
- `name`: Display name for the tenant
- `domain`: Primary domain for the tenant
- `adminUsername`: Username for the admin user
- `adminEmail`: Email for the admin user
- `adminFirstName`: First name of the admin user
- `adminLastName`: Last name of the admin user
- `adminPassword`: Password for the admin user

### Optional Fields

- `description`: Description of the tenant
- `subscriptionPlan`: Subscription plan (BASIC, PREMIUM, ENTERPRISE, CUSTOM)
- `maxUsers`: Maximum number of users allowed
- `customDomain`: Custom domain for the tenant
- `logoUrl`: URL to the tenant logo
- `primaryColor`: Primary color for branding
- `secondaryColor`: Secondary color for branding
- `timezone`: Timezone (default: UTC)
- `locale`: Locale (default: en_US)
- `adminPhoneNumber`: Phone number of the admin user
- `adminDepartment`: Department of the admin user
- `adminJobTitle`: Job title of the admin user
- `enabledModules`: List of modules to enable
- `configurations`: Additional tenant configurations

### Available Modules

- `USER_MANAGEMENT`: User management functionality
- `ROLE_MANAGEMENT`: Role and permission management
- `TENANT_CONFIGURATION`: Tenant configuration management
- `AUDIT_LOGS`: Audit logging functionality
- `REPORTING`: Reporting and analytics
- `ANALYTICS`: Advanced analytics
- `NOTIFICATIONS`: Notification system
- `INTEGRATIONS`: Third-party integrations

## Response

### Success Response (201 Created)

```json
{
  "tenantId": "550e8400-e29b-41d4-a716-446655440000",
  "tenantGeneratedId": "TNT1234567",
  "tenantCode": "COMPANY001",
  "tenantName": "Acme Corporation",
  "domain": "acme.com",
  "status": "ACTIVE",
  "createdAt": "2024-01-15T10:30:00",
  "adminUserId": "550e8400-e29b-41d4-a716-446655440001",
  "adminGeneratedId": "USR1234567",
  "adminUsername": "admin",
  "adminEmail": "admin@acme.com",
  "adminFullName": "John Doe",
  "enabledModules": [
    "USER_MANAGEMENT",
    "ROLE_MANAGEMENT",
    "TENANT_CONFIGURATION"
  ],
  "enabledFeatures": [
    "USER_MANAGEMENT",
    "ROLE_MANAGEMENT",
    "TENANT_CONFIGURATION"
  ],
  "message": "Tenant onboarded successfully",
  "success": true
}
```

### Error Response (400 Bad Request)

```json
{
  "success": false,
  "message": "Tenant code already exists: COMPANY001"
}
```

## What Happens During Onboarding

1. **Tenant Creation**: Creates a new tenant with the provided details and generates a unique 10-character ID (e.g., TNT1234567)
2. **Module Activation**: Enables the specified modules/features for the tenant with generated feature IDs (e.g., TNF1234567)
3. **Admin User Creation**: Creates an admin user with the provided credentials and generates a unique 10-character ID (e.g., USR1234567)
4. **Role Assignment**: Creates a TENANT_ADMIN role with generated ID (e.g., ROL1234567) and assigns it to the admin user
5. **Permission Setup**: Sets up appropriate permissions for the admin role

## Generated IDs

The system generates unique 10-character IDs for all entities:

- **Tenants**: `TNT` + 7 random characters (e.g., TNT1234567)
- **Users**: `USR` + 7 random characters (e.g., USR1234567)
- **Roles**: `ROL` + 7 random characters (e.g., ROL1234567)
- **Features**: `FEA` + 7 random characters (e.g., FEA1234567)
- **Tenant Features**: `TNF` + 7 random characters (e.g., TNF1234567)
- **User-Tenant-Role**: `UTR` + 7 random characters (e.g., UTR1234567)

These generated IDs are used for all internal references while maintaining UUID as the primary key for database operations.

## Admin Role Permissions

The created admin user will have the following permissions:

- `USER_CREATE`: Create new users
- `USER_READ`: Read user information
- `USER_UPDATE`: Update user information
- `USER_DELETE`: Delete users
- `ROLE_CREATE`: Create new roles
- `ROLE_READ`: Read role information
- `ROLE_UPDATE`: Update role information
- `ROLE_DELETE`: Delete roles
- `TENANT_CONFIG_READ`: Read tenant configuration
- `TENANT_CONFIG_UPDATE`: Update tenant configuration

## Example Usage

### cURL

```bash
curl -X POST http://localhost:8080/api/v1/tenants/onboard \
  -H "Content-Type: application/json" \
  -d '{
    "tenantCode": "COMPANY001",
    "name": "Acme Corporation",
    "domain": "acme.com",
    "adminUsername": "admin",
    "adminEmail": "admin@acme.com",
    "adminFirstName": "John",
    "adminLastName": "Doe",
    "adminPassword": "securePassword123",
    "enabledModules": ["USER_MANAGEMENT", "ROLE_MANAGEMENT"]
  }'
```

### JavaScript

```javascript
const response = await fetch('/api/v1/tenants/onboard', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    tenantCode: 'COMPANY001',
    name: 'Acme Corporation',
    domain: 'acme.com',
    adminUsername: 'admin',
    adminEmail: 'admin@acme.com',
    adminFirstName: 'John',
    adminLastName: 'Doe',
    adminPassword: 'securePassword123',
    enabledModules: ['USER_MANAGEMENT', 'ROLE_MANAGEMENT']
  })
});

const result = await response.json();
console.log(result);
```

## Error Handling

The API will return appropriate error messages for:

- Duplicate tenant codes
- Duplicate domains
- Duplicate usernames within the same tenant
- Duplicate emails within the same tenant
- Missing required fields
- Invalid data formats

## Security Considerations

- Passwords are automatically encrypted using BCrypt
- All database operations are wrapped in transactions
- Input validation is performed on all fields
- Unique constraints are enforced at the database level
