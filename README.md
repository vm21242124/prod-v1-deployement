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

### User Service
- **Health**: http://localhost:8081/api/users/health
- **Test**: http://localhost:8081/api/users/test
- **All Users**: http://localhost:8081/api/users
- **User by ID**: http://localhost:8081/api/users/{id}

### Gateway Routes
- **User Service**: http://localhost:8080/api/users/**
- **Product Service**: http://localhost:8080/api/products/**
- **Order Service**: http://localhost:8080/api/orders/**
- **Auth Service**: http://localhost:8080/api/auth/**

## 🔧 Configuration Files

### Key Configuration Changes Made

1. **User Service Application Name**: Fixed conflicting names in `application.properties`
2. **Gateway Routes**: Updated to use service discovery (`lb://user-service`)
3. **Circuit Breakers**: Added for all services
4. **Logging**: Enhanced for debugging

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
- ✅ **Circuit Breakers** for fault tolerance
- ✅ **Health Checks** for all services
- ✅ **Load Balancing** with Ribbon
- ✅ **CORS Configuration** for web clients
- ✅ **Rate Limiting** (Redis-based)
- ✅ **Monitoring** with Actuator endpoints

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
