package com.vitaliy.apigateway.config.security;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements GatewayFilter {

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        if (!jwtService.isTokenValid(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        Long userIdValue = jwtService.extractUserId(token);

        if (userIdValue == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        String userId = String.valueOf(userIdValue);

        String path = exchange.getRequest().getPath().toString();
        String method = exchange.getRequest().getMethod().name();
        String role = jwtService.extractRole(token);

        if (role == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

// Проверка прав доступа
        boolean isManagerAction =
                path.startsWith("/api/v1/products")
                        && (method.equals("POST")
                        || method.equals("PUT"));

        boolean isAdminAction =
                (path.startsWith("/api/v1/products")
                        && method.equals("DELETE"))
                        ||
                        path.startsWith("/api/v1/users/admin");


        if (isManagerAction
                && !("ROLE_MANAGER".equals(role) || "ROLE_ADMIN".equals(role))) {

            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }

        if (isAdminAction && !"ROLE_ADMIN".equals(role)) {

            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }

        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header("X-User-Id", userId)
                .header("X-User-Role", role)
                .build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }
}
