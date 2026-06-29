package com.vitaliy.apigateway.config.security;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
    private final JwtAuthFilter jwtAuthFilter;

    public JwtAuthGatewayFilterFactory(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Override
    public GatewayFilter apply(Object config) {
        return jwtAuthFilter;
    }
}
