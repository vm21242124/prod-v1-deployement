# Use Eclipse Temurin OpenJDK 17 as base image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the Maven wrapper and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Install necessary packages for Alpine
RUN apk add --no-cache bash

# Make mvnw executable
RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the application
RUN ./mvnw clean package -DskipTests

# Create a new stage for runtime
FROM eclipse-temurin:17-jre-alpine

# Install bash for Alpine
RUN apk add --no-cache bash

# Set working directory
WORKDIR /app

# Copy the built jar from the previous stage
COPY --from=0 /app/target/*.jar app.jar

# Expose port
EXPOSE 8761

# Set JVM options
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
