# 🚀 Deployment Guide

This guide explains how to deploy the microservices application using Docker Hub and GitHub Actions.

## 📋 Prerequisites

- Docker and Docker Compose installed
- Docker Hub account
- GitHub repository with the application code

## 🔧 Setup

### 1. Docker Hub Configuration

1. Create a Docker Hub account at [hub.docker.com](https://hub.docker.com)
2. Create a Docker Hub Access Token:
   - Go to Account Settings → Security
   - Click "New Access Token"
   - Give it a name (e.g., "GitHub Actions")
   - Copy the token

### 2. GitHub Secrets Configuration

Add the following secrets to your GitHub repository:

1. Go to your repository → Settings → Secrets and variables → Actions
2. Add these secrets:
   - `DOCKERHUB_USERNAME`: Your Docker Hub username
   - `DOCKERHUB_TOKEN`: Your Docker Hub access token

### 3. Repository Structure

Ensure your repository has the following structure:
```
├── .github/
│   └── workflows/
│       └── deploy.yml
├── apigateway/
│   ├── Dockerfile
│   └── src/
├── eureka/
│   ├── Dockerfile
│   └── src/
├── userService/
│   ├── Dockerfile
│   └── src/
├── docker-compose.yml
├── docker-compose.prod.yml
├── deploy.sh
└── README.md
```

## 🔄 Automated Deployment

### GitHub Actions Workflow

The `.github/workflows/deploy.yml` file automatically:

1. **Triggers on:**
   - Push to main/master branch
   - Pull requests to main/master branch
   - Manual workflow dispatch

2. **Builds and pushes:**
   - API Gateway image
   - Eureka Server image
   - User Service image

3. **Versioning:**
   - Auto-generates version tags (YYYY.MM.DD-HHMMSS format)
   - Tags images with both version and `latest`
   - Creates GitHub releases

4. **Updates:**
   - Updates docker-compose.yml with new image tags
   - Commits changes back to repository

### Manual Deployment

To deploy manually:

1. **Trigger workflow:**
   - Go to Actions tab in GitHub
   - Select "Build and Deploy to Docker Hub"
   - Click "Run workflow"

2. **Monitor progress:**
   - Watch the workflow execution
   - Check logs for any errors
   - Verify images are pushed to Docker Hub

## 🐳 Local Deployment

### Using Production Images

1. **Set environment variable:**
   ```bash
   export DOCKERHUB_USERNAME=your-dockerhub-username
   ```

2. **Deploy using script:**
   ```bash
   chmod +x deploy.sh
   ./deploy.sh
   ```

3. **Or use docker-compose directly:**
   ```bash
   docker-compose -f docker-compose.prod.yml up -d
   ```

### Using Specific Version

```bash
# Deploy specific version
./deploy.sh 2024.01.15-143022

# Or set environment variable
export VERSION=2024.01.15-143022
./deploy.sh
```

## 📊 Service URLs

After deployment, services are available at:

- **API Gateway:** http://localhost:8080
- **Eureka Dashboard:** http://localhost:8761
- **User Service:** http://localhost:8081
- **PostgreSQL:** localhost:5432
- **Redis:** localhost:6379

## 🛠️ Management Commands

### Using deploy.sh script:

```bash
# Show help
./deploy.sh help

# View logs
./deploy.sh logs

# Stop services
./deploy.sh stop

# Start services
./deploy.sh start

# Restart services
./deploy.sh restart

# Check health
./deploy.sh health

# Show URLs
./deploy.sh urls
```

### Using docker-compose directly:

```bash
# View logs
docker-compose -f docker-compose.prod.yml logs -f

# Stop services
docker-compose -f docker-compose.prod.yml down

# Start services
docker-compose -f docker-compose.prod.yml up -d

# Restart specific service
docker-compose -f docker-compose.prod.yml restart api-gateway

# View service status
docker-compose -f docker-compose.prod.yml ps
```

## 🔍 Monitoring and Troubleshooting

### Health Checks

The deployment script includes health checks for:
- API Gateway health endpoint
- Eureka Server availability
- User Service health endpoint

### Common Issues

1. **Port conflicts:**
   - Ensure ports 8080, 8081, 8761, 5432, 6379 are available
   - Stop conflicting services

2. **Docker Hub authentication:**
   - Verify DOCKERHUB_USERNAME and DOCKERHUB_TOKEN are set
   - Check Docker Hub access token permissions

3. **Service startup order:**
   - Services have proper dependencies configured
   - Wait for database and Eureka to start before other services

### Logs and Debugging

```bash
# View all logs
docker-compose -f docker-compose.prod.yml logs

# View specific service logs
docker-compose -f docker-compose.prod.yml logs api-gateway

# Follow logs in real-time
docker-compose -f docker-compose.prod.yml logs -f

# View container details
docker-compose -f docker-compose.prod.yml ps
```

## 🔒 Security Considerations

1. **Environment Variables:**
   - Use `.env` files for sensitive data
   - Never commit secrets to repository
   - Use Docker secrets for production

2. **Network Security:**
   - Services communicate over internal Docker network
   - Only necessary ports exposed to host
   - Consider using reverse proxy for production

3. **Image Security:**
   - Regularly update base images
   - Scan images for vulnerabilities
   - Use multi-stage builds to reduce attack surface

## 📈 Scaling

### Horizontal Scaling

```bash
# Scale specific service
docker-compose -f docker-compose.prod.yml up -d --scale user-service=3

# Scale with load balancer
docker-compose -f docker-compose.prod.yml up -d --scale api-gateway=2
```

### Production Considerations

1. **Load Balancing:**
   - Use nginx or HAProxy for load balancing
   - Configure sticky sessions if needed

2. **Database:**
   - Use managed database service
   - Configure backups and replication

3. **Monitoring:**
   - Implement application monitoring (Prometheus, Grafana)
   - Set up log aggregation (ELK stack)

## 🆘 Support

For issues and questions:

1. Check the logs using `./deploy.sh logs`
2. Verify Docker Hub credentials
3. Ensure all prerequisites are met
4. Check GitHub Actions workflow status

## 📝 Version History

- **v1.0.0:** Initial deployment setup
- **v1.1.0:** Added health checks and monitoring
- **v1.2.0:** Improved error handling and logging
