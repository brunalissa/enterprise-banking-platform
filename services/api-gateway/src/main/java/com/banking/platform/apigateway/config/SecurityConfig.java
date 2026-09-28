package com.banking.platform.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import reactor.core.publisher.Mono;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
            @Value("${jwt.secret}") String secret) {
        var key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        var bearer = new AuthenticationWebFilter((org.springframework.security.authentication.ReactiveAuthenticationManager) authentication -> Mono.fromCallable(() -> {
            var claims = Jwts.parser().verifyWith(key).build()
                    .parseSignedClaims(authentication.getCredentials().toString()).getPayload();
            if (claims.getSubject() == null || claims.getExpiration() == null) {
                throw new BadCredentialsException("JWT subject and expiration are required");
            }
            String role = claims.get("role", String.class);
            if (role == null || role.isBlank()) {
                throw new BadCredentialsException("JWT role is required");
            }
            return (org.springframework.security.core.Authentication)
                    new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + role)));
        }).onErrorMap(error -> new BadCredentialsException("Invalid bearer token", error)));
        bearer.setServerAuthenticationConverter(exchange -> {
            String header = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (header == null || !header.startsWith("Bearer ")) return Mono.empty();
            return Mono.just(new UsernamePasswordAuthenticationToken("bearer", header.substring(7)));
        });
        http
            .addFilterAt(bearer, SecurityWebFiltersOrder.AUTHENTICATION)
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .exceptionHandling(errors -> errors.authenticationEntryPoint(new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED)))
            .authorizeExchange(exchange -> exchange
                .pathMatchers("/api/v1/auth/**", "/actuator/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyExchange().authenticated()
            )
            .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
            .formLogin(ServerHttpSecurity.FormLoginSpec::disable);
        return http.build();
    }
}
