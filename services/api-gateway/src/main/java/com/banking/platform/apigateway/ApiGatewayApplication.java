package com.banking.platform.apigateway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

@SpringBootApplication
public class ApiGatewayApplication {
    public static void main(String[] args) { SpringApplication.run(ApiGatewayApplication.class, args); }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("auth", r -> r.path("/api/v1/auth/**").uri("http://localhost:8081"))
            .route("customer", r -> r.path("/api/v1/customers/**").uri("http://localhost:8082"))
            .route("account", r -> r.path("/api/v1/accounts/**").uri("http://localhost:8083"))
            .route("transaction", r -> r.path("/api/v1/transactions/**").uri("http://localhost:8084"))
            .route("payment", r -> r.path("/api/v1/payments/**").uri("http://localhost:8085"))
            .route("notification", r -> r.path("/api/v1/notifications/**").uri("http://localhost:8086"))
            .route("fraud", r -> r.path("/api/v1/fraud/**").uri("http://localhost:8087"))
            .build();
    }
}
