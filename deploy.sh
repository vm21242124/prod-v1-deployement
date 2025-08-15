#!/bin/bash

# Deployment script for the microservices application
# Usage: ./deploy.sh [version]

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
DOCKERHUB_USERNAME=${DOCKERHUB_USERNAME:-"your-dockerhub-username"}
VERSION=${1:-"latest"}

echo -e "${BLUE}🚀 Starting deployment...${NC}"
echo -e "${YELLOW}Docker Hub Username: $DOCKERHUB_USERNAME${NC}"
echo -e "${YELLOW}Version: $VERSION${NC}"

# Function to check if Docker is running
check_docker() {
    if ! docker info > /dev/null 2>&1; then
        echo -e "${RED}❌ Docker is not running. Please start Docker and try again.${NC}"
        exit 1
    fi
    echo -e "${GREEN}✅ Docker is running${NC}"
}

# Function to check if docker-compose is available
check_docker_compose() {
    if ! command -v docker-compose &> /dev/null; then
        echo -e "${RED}❌ docker-compose is not installed. Please install it and try again.${NC}"
        exit 1
    fi
    echo -e "${GREEN}✅ docker-compose is available${NC}"
}

# Function to pull latest images
pull_images() {
    echo -e "${BLUE}📥 Pulling latest images...${NC}"
    
    # Set environment variable for docker-compose
    export DOCKERHUB_USERNAME=$DOCKERHUB_USERNAME
    
    # Pull images
    docker-compose -f docker-compose.prod.yml pull
    
    echo -e "${GREEN}✅ Images pulled successfully${NC}"
}

# Function to stop existing containers
stop_containers() {
    echo -e "${BLUE}🛑 Stopping existing containers...${NC}"
    docker-compose -f docker-compose.prod.yml down
    echo -e "${GREEN}✅ Containers stopped${NC}"
}

# Function to start containers
start_containers() {
    echo -e "${BLUE}🚀 Starting containers...${NC}"
    docker-compose -f docker-compose.prod.yml up -d
    echo -e "${GREEN}✅ Containers started${NC}"
}

# Function to check service health
check_health() {
    echo -e "${BLUE}🏥 Checking service health...${NC}"
    
    # Wait for services to be ready
    sleep 30
    
    # Check API Gateway
    if curl -f http://localhost:8080/actuator/health > /dev/null 2>&1; then
        echo -e "${GREEN}✅ API Gateway is healthy${NC}"
    else
        echo -e "${YELLOW}⚠️  API Gateway health check failed (may still be starting)${NC}"
    fi
    
    # Check Eureka Server
    if curl -f http://localhost:8761 > /dev/null 2>&1; then
        echo -e "${GREEN}✅ Eureka Server is running${NC}"
    else
        echo -e "${YELLOW}⚠️  Eureka Server check failed (may still be starting)${NC}"
    fi
    
    # Check User Service
    if curl -f http://localhost:8081/actuator/health > /dev/null 2>&1; then
        echo -e "${GREEN}✅ User Service is healthy${NC}"
    else
        echo -e "${YELLOW}⚠️  User Service health check failed (may still be starting)${NC}"
    fi
}

# Function to show service URLs
show_urls() {
    echo -e "${BLUE}🌐 Service URLs:${NC}"
    echo -e "${GREEN}API Gateway:${NC} http://localhost:8080"
    echo -e "${GREEN}Eureka Dashboard:${NC} http://localhost:8761"
    echo -e "${GREEN}User Service:${NC} http://localhost:8081"
    echo -e "${GREEN}PostgreSQL:${NC} localhost:5432"
    echo -e "${GREEN}Redis:${NC} localhost:6379"
}

# Function to show logs
show_logs() {
    echo -e "${BLUE}📋 Recent logs:${NC}"
    docker-compose -f docker-compose.prod.yml logs --tail=20
}

# Main deployment process
main() {
    echo -e "${BLUE}🔍 Pre-deployment checks...${NC}"
    check_docker
    check_docker_compose
    
    echo -e "${BLUE}📦 Deployment process...${NC}"
    pull_images
    stop_containers
    start_containers
    
    echo -e "${BLUE}🔍 Post-deployment checks...${NC}"
    check_health
    show_urls
    
    echo -e "${GREEN}🎉 Deployment completed successfully!${NC}"
    echo -e "${YELLOW}💡 To view logs: docker-compose -f docker-compose.prod.yml logs -f${NC}"
    echo -e "${YELLOW}💡 To stop services: docker-compose -f docker-compose.prod.yml down${NC}"
}

# Handle command line arguments
case "${1:-}" in
    "logs")
        show_logs
        ;;
    "stop")
        stop_containers
        ;;
    "start")
        start_containers
        ;;
    "restart")
        stop_containers
        start_containers
        ;;
    "health")
        check_health
        ;;
    "urls")
        show_urls
        ;;
    "help"|"-h"|"--help")
        echo "Usage: $0 [version|logs|stop|start|restart|health|urls|help]"
        echo ""
        echo "Commands:"
        echo "  version   - Deploy specific version (default: latest)"
        echo "  logs      - Show recent logs"
        echo "  stop      - Stop all services"
        echo "  start     - Start all services"
        echo "  restart   - Restart all services"
        echo "  health    - Check service health"
        echo "  urls      - Show service URLs"
        echo "  help      - Show this help message"
        echo ""
        echo "Environment variables:"
        echo "  DOCKERHUB_USERNAME - Your Docker Hub username"
        exit 0
        ;;
    *)
        main
        ;;
esac
