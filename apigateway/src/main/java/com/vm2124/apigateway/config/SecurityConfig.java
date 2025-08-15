package com.vm2124.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
            .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
            .authorizeExchange(exchanges -> exchanges
                // Public endpoints (no authentication required)
                .pathMatchers("/actuator/health", "/actuator/info").permitAll()
                .pathMatchers("/api/auth/login", "/api/auth/register").permitAll()
                .pathMatchers("/test/**").permitAll()
                
                // Protected endpoints (authentication required)
                .pathMatchers("/api/users/**").hasAnyRole("USER", "ADMIN")
                .pathMatchers("/api/products/**").hasAnyRole("USER", "ADMIN")
                .pathMatchers("/api/orders/**").hasAnyRole("USER", "ADMIN")
                
                // Admin-only endpoints
                .pathMatchers("/api/admin/**").hasRole("ADMIN")
                
                // All other requests require authentication
                .anyExchange().authenticated()
            )
            .securityContextRepository(securityContextRepository());
        
        return http.build();
    }

    @Bean
    public ServerSecurityContextRepository securityContextRepository() {
        return new WebSessionServerSecurityContextRepository();
    }
}
