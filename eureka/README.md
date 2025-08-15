# Eureka Server

A Spring Cloud Netflix Eureka server for service discovery in a microservices architecture.

## Features

- **Service Registry**: Centralized service registration and discovery
- **Health Monitoring**: Automatic health checks for registered services
- **Load Balancing**: Client-side load balancing support
- **High Availability**: Support for multiple Eureka server instances
- **Dashboard**: Web-based dashboard for service monitoring

## Quick Start

### Using Docker

```bash
docker build -t eureka-server .
docker run -p 8761:8761 eureka-server
```

### Manual Setup

```bash
./mvnw spring-boot:run
```

## Configuration

### Application Properties

- **Port**: 8761 (default)
- **Hostname**: localhost
- **Self Preservation**: Disabled for development
- **Health Checks**: Enabled with detailed information

### Environment Variables

- `SERVER_PORT`: Application port (default: 8761)
- `EUREKA_INSTANCE_HOSTNAME`: Eureka server hostname

## Dashboard

Access the Eureka dashboard at: http://localhost:8761

### Dashboard Features

- **Instances**: View all registered service instances
- **System Status**: Monitor Eureka server health
- **Environment**: View server environment information
- **Health**: Check service health status

## Service Registration

Services can register with Eureka using the following configuration:

```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
    register-with-eureka: true
    fetch-registry: true
  instance:
    prefer-ip-address: true
    instance-id: ${spring.application.name}:${server.port}
```

## Health Checks

### Health Endpoints

- `GET /actuator/health` - Spring Boot health check
- `GET /eureka/apps` - List all registered applications
- `GET /eureka/apps/{app-name}` - Get specific application info

## Monitoring

### Metrics

Eureka provides various metrics for monitoring:

- **Registration Count**: Number of service registrations
- **Renewal Count**: Number of service renewals
- **Cancel Count**: Number of service cancellations
- **Instance Count**: Number of active instances

## Development

### Building

```bash
./mvnw clean package
```

### Running Tests

```bash
./mvnw test
```

## Troubleshooting

### Common Issues

1. **Service Not Registering**: Check if Eureka server is running and accessible
2. **Connection Refused**: Verify port 8761 is not blocked by firewall
3. **Health Check Failures**: Ensure services are properly configured

### Logs

Enable debug logging:

```properties
logging.level.com.netflix.eureka=DEBUG
logging.level.com.netflix.discovery=DEBUG
```

## Production Considerations

- Enable self-preservation mode
- Configure multiple Eureka server instances
- Set up proper security
- Monitor server performance
- Configure backup and recovery procedures
