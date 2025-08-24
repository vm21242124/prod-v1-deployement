# Microservices Architecture - Service Management

This repository contains a complete microservices architecture with service discovery, API gateway, and user management. This README provides comprehensive instructions for running and managing all services locally.

## 🏗️ Architecture Overview

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   API Gateway   │    │   User Service  │    │  Eureka Server  │
│   (Port: 8080)  │◄──►│   (Port: 8081)  │◄──►│  (Port: 8761)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   PostgreSQL    │    │     Redis       │    │   Circuit       │
│   (Port: 5432)  │    │   (Port: 6379)  │    │   Breakers      │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 📋 Prerequisites

Before running the services, ensure you have:

- **Java 11 or higher** installed and in your PATH
- **Maven** (included via Maven wrapper)
- **PostgreSQL** (optional - can use Docker)
- **PowerShell** (for Windows users)

## 🚀 Quick Start

### Option 1: PowerShell Script (Recommended)

```powershell
# Start all services
.\start-all-services.ps1

# Start in background mode (each service in separate window)
.\start-all-services.ps1 -Background

# Skip tests after startup
.\start-all-services.ps1 -SkipTests
```

### Option 2: Batch File

```cmd
# Start all services (Windows)
start-all-services.bat
```

### Option 3: Docker Compose

```bash
# Start all services with Docker
cd apigateway
docker-compose up -d
```

## 🛑 Stopping Services

### PowerShell Script

```powershell
# Graceful shutdown
.\stop-all-services.ps1

# Force shutdown
.\stop-all-services.ps1 -Force
```

### Manual Stop

```cmd
# Stop by closing individual terminal windows
# Or use Ctrl+C in each service terminal
```

## 🧪 Testing Services

### Automated Testing

```powershell
# Run comprehensive tests
.\test-services.ps1
```

### Manual Testing

```bash
# Test Eureka Dashboard
curl http://localhost:8761

# Test API Gateway
curl http://localhost:8080/actuator/health

# Test User Service directly
curl http://localhost:8081/api/users/health

# Test routing through Gateway
curl http://localhost:8080/api/users/health

# Test User Service endpoints
curl http://localhost:8080/api/users/test
curl http://localhost:8080/api/users
```

## 📊 Service Endpoints

### Eureka Server
- **Dashboard**: http://localhost:8761
- **Health**: http://localhost:8761/actuator/health
- **Applications**: http://localhost:8761/eureka/apps

### API Gateway
- **Health**: http://localhost:8080/actuator/health
- **Routes**: http://localhost:8080/actuator/gateway/routes
- **Metrics**: http://localhost:8080/actuator/metrics
- **Fallback Endpoints**: http://localhost:8080/fallback/{service-name}

### User Service
- **Health**: http://localhost:8081/api/users/health
- **Test**: http://localhost:8081/api/users/test
- **All Users**: http://localhost:8081/api/users
- **User by ID**: http://localhost:8081/api/users/{id}
- **User Info**: http://localhost:8081/api/users/{userId}/info
- **Token Validation**: http://localhost:8081/api/auth/validate
- **Current User Info**: http://localhost:8081/api/auth/me

### Gateway Routes
- **User Service**: http://localhost:8080/api/users/**
- **Product Service**: http://localhost:8080/api/products/**
- **Order Service**: http://localhost:8080/api/orders/**
- **Auth Service**: http://localhost:8080/api/auth/**

### Authentication Endpoints

#### Login
```
POST /api/auth/login
```

**Request Body:**
```json
{
  "email": "admin@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresAt": "2024-01-16T10:30:00",
  "user": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "generatedId": "USR1234567",
    "username": "admin@example.com",
    "email": "admin@example.com",
    "firstName": "Admin",
    "lastName": "User",
    "isActive": true,
    "tenantId": "TNT1234567",
    "tenantGeneratedId": "TNT1234567",
    "roles": ["ROL1234567"],
    "lastLoginAt": "2024-01-15T10:30:00"
  }
}
```

**Error Response:**
```json
{
  "success": false,
  "message": "Invalid email or password",
  "token": null,
  "tokenType": null,
  "expiresAt": null,
  "user": null
}
```

#### Validate Token (Enhanced)
```
GET /api/auth/validate
```

**Headers:**
```
Authorization: Bearer <token>
```

**Response:**
```json
{
  "valid": true,
  "message": "User information retrieved successfully",
  "user": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "generatedId": "USR1234567",
    "username": "admin@example.com",
    "email": "admin@example.com",
    "firstName": "Admin",
    "lastName": "User",
    "isActive": true,
    "tenantId": "TNT1234567",
    "tenantGeneratedId": "TNT1234567",
    "createdAt": "2024-01-15T10:30:00",
    "lastLoginAt": "2024-01-15T10:30:00"
  },
  "tenant": {
    "id": "456e7890-e89b-12d3-a456-426614174000",
    "generatedId": "TNT1234567",
    "name": "Example Corp",
    "domain": "example.com",
    "status": "ACTIVE",
    "plan": "PREMIUM",
    "createdAt": "2024-01-15T10:30:00",
    "enabledModules": [
      "USER_MANAGEMENT",
      "ROLE_MANAGEMENT",
      "SECURITY",
      "REPORTING"
    ],
    "configuration": {
      "maxUsers": 100,
      "storageLimit": "10GB",
      "isActive": true
    }
  },
  "roles": [
    {
      "id": "789e0123-e89b-12d3-a456-426614174000",
      "generatedId": "ROL1234567",
      "roleCode": "TENANT_ADMIN",
      "roleName": "Tenant Administrator",
      "description": "Tenant administrator with tenant-specific permissions",
      "priority": 800,
      "roleType": "TENANT_ADMIN",
      "isSystemRole": true,
      "isDefault": false,
      "permissions": [
        "USER_CREATE",
        "USER_READ",
        "USER_UPDATE",
        "ROLE_CREATE",
        "ROLE_READ",
        "ROLE_UPDATE",
        "TENANT_CONFIG_READ",
        "TENANT_CONFIG_UPDATE"
      ]
    }
  ],
  "permissions": [
    "USER_CREATE",
    "USER_READ",
    "USER_UPDATE",
    "USER_ACTIVATE",
    "USER_DEACTIVATE",
    "ROLE_CREATE",
    "ROLE_READ",
    "ROLE_UPDATE",
    "ROLE_ASSIGN",
    "ROLE_REMOVE",
    "TENANT_CONFIG_READ",
    "TENANT_CONFIG_UPDATE",
    "FEATURE_READ",
    "FEATURE_UPDATE",
    "AUDIT_READ",
    "REPORT_CREATE",
    "REPORT_READ",
    "REPORT_UPDATE",
    "REPORT_EXPORT"
  ],
  "metadata": {
    "timestamp": "2024-01-15T10:30:00.000Z",
    "totalRoles": 1,
    "totalPermissions": 19
  }
}
```

#### Get Current User Info (from Context)
```
GET /api/auth/me
```

**Headers (set by API Gateway):**
```
X-User-ID: 123e4567-e89b-12d3-a456-426614174000
X-User-Generated-ID: USR1234567
X-Username: admin@example.com
X-Email: admin@example.com
X-First-Name: Admin
X-Last-Name: User
X-Is-Active: true
X-Tenant-ID: TNT1234567
X-Tenant-Generated-ID: TNT1234567
X-User-Roles: ["ROL1234567"]
X-User-Permissions: ["USER_CREATE", "USER_READ", "USER_UPDATE"]
```

**Response:**
```json
{
  "success": true,
  "message": "User information retrieved from context",
  "user": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "generatedId": "USR1234567",
    "username": "admin@example.com",
    "email": "admin@example.com",
    "firstName": "Admin",
    "lastName": "User",
    "isActive": true,
    "tenantId": "TNT1234567",
    "tenantGeneratedId": "TNT1234567",
    "createdAt": "2024-01-15T10:30:00",
    "lastLoginAt": "2024-01-15T10:30:00"
  },
  "permissions": [
    "USER_CREATE",
    "USER_READ",
    "USER_UPDATE"
  ]
}
```

## 🔧 Configuration Files

### Key Configuration Changes Made

1. **User Service Application Name**: Fixed conflicting names in `application.properties`
2. **Gateway Routes**: Updated to use service discovery (`lb://user-service`)
3. **JWT Authentication**: Implemented in API Gateway with automatic user context extraction
4. **Circuit Breakers**: Added for all services
5. **Logging**: Enhanced for debugging

### Configuration Files

- `apigateway/src/main/resources/application.yml` - Gateway configuration
- `userService/src/main/resources/application.yml` - User service configuration
- `userService/src/main/resources/application.properties` - User service properties
- `eureka/src/main/resources/application.yml` - Eureka server configuration

## 🐛 Troubleshooting

### Common Issues

#### 1. Service Not Starting
```powershell
# Check Java installation
java -version

# Check Maven wrapper
ls eureka/mvnw

# Check port availability
netstat -an | findstr ":8761"
```

#### 2. Services Not Registering with Eureka
- Ensure Eureka server starts first
- Check application names match
- Verify network connectivity
- Check logs for registration errors

#### 3. Gateway Can't Route to Services
- Verify services are registered with Eureka
- Check gateway route configuration
- Ensure services are healthy
- Check circuit breaker status

#### 4. Database Connection Issues
```bash
# Check PostgreSQL
docker ps | grep postgres

# Or check local PostgreSQL
psql -h localhost -U postgres -d user-service
```

### Debug Commands

```powershell
# Check service status
.\test-services.ps1

# Check Eureka registrations
curl http://localhost:8761/eureka/apps

# Check Gateway routes
curl http://localhost:8080/actuator/gateway/routes

# Check service health
curl http://localhost:8081/actuator/health
curl http://localhost:8080/actuator/health
```

### Logs to Monitor

- **Eureka**: Service registrations and heartbeats
- **User Service**: Database connections and startup
- **API Gateway**: Route matching and service discovery

## 📁 Script Files

| File | Description | Usage |
|------|-------------|-------|
| `start-all-services.ps1` | PowerShell script to start all services | `.\start-all-services.ps1` |
| `start-all-services.bat` | Batch file to start all services | `start-all-services.bat` |
| `stop-all-services.ps1` | PowerShell script to stop all services | `.\stop-all-services.ps1` |
| `test-services.ps1` | Comprehensive service testing | `.\test-services.ps1` |
| `STARTUP_GUIDE.md` | Detailed startup instructions | Reference guide |

## 🔄 Service Startup Sequence

1. **Eureka Server** (Port 8761) - Service discovery
2. **User Service** (Port 8081) - User management
3. **API Gateway** (Port 8080) - Routing and circuit breakers

## 🎯 Features

- ✅ **Service Discovery** with Eureka
- ✅ **API Gateway** with Spring Cloud Gateway
- ✅ **JWT Authentication** with automatic user context extraction
- ✅ **Circuit Breakers** for fault tolerance
- ✅ **Health Checks** for all services
- ✅ **Load Balancing** with Ribbon
- ✅ **CORS Configuration** for web clients
- ✅ **Rate Limiting** (Redis-based)
- ✅ **Monitoring** with Actuator endpoints

## 🔐 API Gateway Authentication Flow

### How It Works

1. **Client Request**: Client sends request with JWT token in Authorization header
2. **Token Validation**: API Gateway validates JWT token and extracts user information
3. **User Service Call**: Gateway calls User Service to get comprehensive user data
4. **Header Injection**: Gateway adds user information as headers to the request
5. **Service Routing**: Request is forwarded to appropriate microservice with user context

### Headers Added by Gateway

```
X-User-ID: 123e4567-e89b-12d3-a456-426614174000
X-User-Generated-ID: USR1234567
X-Username: admin@example.com
X-Email: admin@example.com
X-First-Name: Admin
X-Last-Name: User
X-Is-Active: true
X-Tenant-ID: TNT1234567
X-Tenant-Generated-ID: TNT1234567
X-User-Roles: ["ROL1234567"]
X-User-Permissions: ["USER_CREATE", "USER_READ", "USER_UPDATE"]
```

### Public Endpoints (No Authentication Required)

- `/api/auth/login` - User login
- `/api/auth/register` - User registration
- `/actuator/**` - Health checks and metrics
- `/health` - Health endpoint
- `/test/**` - Test endpoints

## 🚀 Next Steps

1. **Add More Services**: Product, Order, Auth services
2. **Implement Security**: OAuth2, JWT tokens
3. **Add Monitoring**: Prometheus, Grafana
4. **Database Migration**: Flyway or Liquibase
5. **API Documentation**: Swagger/OpenAPI
6. **Containerization**: Docker images for each service

## 📞 Support

If you encounter issues:

1. Check the troubleshooting section above
2. Review service logs for error messages
3. Verify all prerequisites are met
4. Ensure ports are not in use by other applications

## 📄 License

This project is for educational and development purposes.
