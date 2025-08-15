# User Service

A Spring Boot microservice for user management with Eureka client integration and comprehensive testing endpoints.

## Features

- **User Management**: CRUD operations for user entities
- **Service Discovery**: Automatic registration with Eureka server
- **Database Integration**: PostgreSQL with JPA/Hibernate
- **Health Checks**: Comprehensive health monitoring
- **Testing Endpoints**: Various endpoints for testing and debugging
- **Circuit Breaker Testing**: Endpoints to test resilience patterns

## Quick Start

### Using Docker

```bash
docker build -t user-service .
docker run -p 8081:8081 user-service
```

### Manual Setup

1. Start PostgreSQL database
2. Update database configuration in `application.yml`
3. Run the application:

```bash
./mvnw spring-boot:run
```

## Configuration

### Application Properties

- **Port**: 8081
- **Database**: PostgreSQL
- **Service Name**: user-service
- **Eureka Registration**: Enabled

### Environment Variables

- `SPRING_DATASOURCE_URL`: Database connection URL
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password
- `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE`: Eureka server URL

## API Endpoints

### Health and Testing

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users/health` | Service health check |
| GET | `/api/users/test` | Test endpoint |
| GET | `/api/users/count` | Get user count |
| GET | `/api/users/slow` | Simulate slow response |
| GET | `/api/users/error` | Simulate error |

### User Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users` | Get all users |
| GET | `/api/users/{id}` | Get user by ID |
| GET | `/api/users/username/{username}` | Get user by username |
| POST | `/api/users` | Create new user |
| PUT | `/api/users/{id}` | Update user |
| DELETE | `/api/users/{id}` | Delete user |

### Validation

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users/check-username/{username}` | Check if username exists |
| GET | `/api/users/check-email/{email}` | Check if email exists |

## Data Model

### User Entity

```java
public class User {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

### User Status

- `ACTIVE`: User account is active
- `INACTIVE`: User account is inactive
- `SUSPENDED`: User account is suspended

## Sample Data

The service automatically initializes with sample users:

1. **john_doe** - john.doe@example.com
2. **jane_smith** - jane.smith@example.com
3. **admin** - admin@example.com

## Testing Endpoints

### Health Check

```bash
curl http://localhost:8081/api/users/health
```

Response:
```json
{
  "status": "UP",
  "service": "User Service",
  "timestamp": 1640995200000,
  "message": "User service is running"
}
```

### Test Endpoint

```bash
curl http://localhost:8081/api/users/test
```

### Get All Users

```bash
curl http://localhost:8081/api/users
```

### Create User

```bash
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "email": "newuser@example.com",
    "firstName": "New",
    "lastName": "User",
    "password": "password123"
  }'
```

### Circuit Breaker Testing

```bash
# Test slow response (5 second delay)
curl http://localhost:8081/api/users/slow

# Test error simulation
curl http://localhost:8081/api/users/error
```

## Database Schema

The service uses PostgreSQL with the following schema:

```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    status VARCHAR(50) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## Service Discovery

The service automatically registers with Eureka server and can be discovered by other services using the service name `user-service`.

## Health Checks

### Actuator Endpoints

- `GET /actuator/health` - Spring Boot health check
- `GET /actuator/info` - Application information
- `GET /actuator/metrics` - Application metrics

## Development

### Building

```bash
./mvnw clean package
```

### Running Tests

```bash
./mvnw test
```

### Database Setup

1. Install PostgreSQL
2. Create database: `userservice`
3. Update connection properties in `application.yml`

## Troubleshooting

### Common Issues

1. **Database Connection**: Ensure PostgreSQL is running and accessible
2. **Eureka Registration**: Check if Eureka server is running
3. **Port Conflicts**: Verify port 8081 is available

### Logs

Enable debug logging:

```properties
logging.level.com.vm2124.userService=DEBUG
```

## Production Considerations

- Configure proper database connection pooling
- Set up database backups
- Implement proper security measures
- Configure monitoring and alerting
- Set up proper logging and tracing
