# API Gateway

A Spring Cloud Gateway-based API gateway that provides routing, rate limiting, circuit breaking, and security features for microservices.

## Features

- **Service Discovery**: Automatic service discovery and routing
- **Rate Limiting**: Redis-based rate limiting with configurable limits
- **Circuit Breaker**: Resilience4j circuit breaker for fault tolerance
- **CORS Support**: Cross-origin resource sharing configuration
- **Security Headers**: Automatic security header injection
- **Health Checks**: Comprehensive health check endpoints
- **Metrics**: Prometheus metrics for monitoring
- **Logging**: Request/response logging
- **Fallback Handling**: Graceful degradation when services are unavailable

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Redis (for rate limiting)
- PostgreSQL (for user service)
- Docker and Docker Compose (optional)

## Quick Start

### Using Docker Compose (Recommended)

1. Clone the repository
2. Navigate to the apigateway directory
3. Run the application:

```bash
docker-compose up -d
```

This will start:
- Eureka Server on port 8761
- API Gateway on port 8080
- User Service on port 8081
- PostgreSQL on port 5432
- Redis on port 6379

### Manual Setup

1. Start Eureka Server:
```bash
cd ../eureka
./mvnw spring-boot:run
```

2. Start User Service:
```bash
cd ../userService
./mvnw spring-boot:run
```

3. Start Redis:
```bash
redis-server
```

4. Build and run the API Gateway:
```bash
cd ../apigateway
./mvnw clean package
java -jar target/apigateway-0.0.1-SNAPSHOT.jar
```

## Configuration

### Application Properties

The main configuration is in `application.yml`:

- **Port**: 8080 (default)
- **Service Discovery**: Enabled with lowercase service IDs
- **CORS**: Configured for all origins
- **Rate Limiting**: Redis-based with service-specific limits
- **Circuit Breaker**: Resilience4j with fallback endpoints

### Environment Variables

- `SPRING_REDIS_HOST`: Redis host (default: localhost)
- `SPRING_REDIS_PORT`: Redis port (default: 6379)
- `SERVER_PORT`: Application port (default: 8080)

## API Routes

The gateway routes requests to microservices based on URL patterns:

| Service | Route Pattern | Description |
|---------|---------------|-------------|
| User Service | `/api/users/**` | User management operations |
| Product Service | `/api/products/**` | Product catalog operations |
| Order Service | `/api/orders/**` | Order management operations |
| Auth Service | `/api/auth/**` | Authentication operations |

### Example Requests

```bash
# User service endpoints
curl http://localhost:8080/api/users/health
curl http://localhost:8080/api/users/test
curl http://localhost:8080/api/users
curl http://localhost:8080/api/users/1
curl http://localhost:8080/api/users/username/john_doe
curl http://localhost:8080/api/users/count
curl http://localhost:8080/api/users/slow
curl http://localhost:8080/api/users/error

# Create a new user
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "email": "newuser@example.com",
    "firstName": "New",
    "lastName": "User",
    "password": "password123"
  }'

# Product service (when available)
curl http://localhost:8080/api/products

# Order service (when available)
curl http://localhost:8080/api/orders

# Auth service (when available)
curl http://localhost:8080/api/auth/login
```

## Health Checks

### Health Endpoints

- `GET /health/status` - Overall service status
- `GET /health/ready` - Readiness probe
- `GET /health/live` - Liveness probe
- `GET /actuator/health` - Spring Boot health check

### Example

```bash
curl http://localhost:8080/health/status
```

Response:
```json
{
  "status": "UP",
  "service": "API Gateway",
  "timestamp": 1640995200000,
  "version": "1.0.0"
}
```

## Monitoring

### Metrics

Prometheus metrics are available at:
- `GET /actuator/metrics` - Available metrics
- `GET /actuator/prometheus` - Prometheus format metrics

### Gateway Information

- `GET /actuator/gateway/routes` - Current route configuration
- `GET /actuator/gateway/globalfilters` - Global filters

## Circuit Breaker

When services are unavailable, the gateway returns fallback responses:

```bash
curl http://localhost:8080/api/users/1
```

Fallback Response:
```json
{
  "message": "User service is currently unavailable",
  "status": "SERVICE_UNAVAILABLE",
  "timestamp": 1640995200000
}
```

## Rate Limiting

Rate limiting is configured per service:

- **User Service**: 10 requests/second, burst capacity 20
- **Product Service**: 15 requests/second, burst capacity 30
- **Order Service**: 5 requests/second, burst capacity 10

When rate limit is exceeded:
```json
{
  "error": "Too Many Requests",
  "message": "Rate limit exceeded"
}
```

## Development

### Building

```bash
./mvnw clean package
```

### Running Tests

```bash
./mvnw test
```

### Running in Development Mode

```bash
./mvnw spring-boot:run
```

## Docker

### Building Image

```bash
docker build -t api-gateway .
```

### Running Container

```bash
docker run -p 8080:8080 api-gateway
```

## Troubleshooting

### Common Issues

1. **Redis Connection Error**: Ensure Redis is running and accessible
2. **Service Not Found**: Check if target services are running and registered
3. **CORS Issues**: Verify CORS configuration matches your frontend requirements

### Logs

Enable debug logging by setting:
```properties
logging.level.com.vm2124.apigateway=DEBUG
logging.level.org.springframework.cloud.gateway=DEBUG
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## License

This project is licensed under the MIT License.
