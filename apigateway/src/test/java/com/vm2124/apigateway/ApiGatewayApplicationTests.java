package com.vm2124.apigateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.cloud.gateway.discovery.locator.enabled=false",
    "spring.redis.host=localhost",
    "spring.redis.port=6379"
})
class ApiGatewayApplicationTests {

    @Test
    void contextLoads() {
        // This test verifies that the Spring context loads successfully
    }
}
