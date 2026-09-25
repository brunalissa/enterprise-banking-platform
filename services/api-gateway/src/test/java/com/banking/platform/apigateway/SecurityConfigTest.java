package com.banking.platform.apigateway;

import com.banking.platform.apigateway.config.SecurityConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@SpringBootTest(classes = {SecurityConfig.class, SecurityConfigTest.TestApp.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"spring.cloud.gateway.enabled=false", "spring.main.web-application-type=reactive",
                "jwt.secret=" + SecurityConfigTest.SECRET})
class SecurityConfigTest {
    static final String SECRET = "test-only-key-with-at-least-thirty-two-characters";
    @LocalServerPort int port;
    private WebTestClient client() {
        return WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
    }
    private String token(Instant expiry) {
        return Jwts.builder().subject("test@example.com").claim("role", "CUSTOMER")
                .expiration(Date.from(expiry))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8))).compact();
    }
    @Test void publicLoginDoesNotRequireAnExistingToken() {
        client().post().uri("/api/v1/auth/login").exchange().expectStatus().isOk();
    }
    @Test void protectedRouteRejectsMissingAndInvalidTokens() {
        client().get().uri("/protected").exchange().expectStatus().isUnauthorized();
        client().get().uri("/protected").headers(h -> h.setBearerAuth("invalid"))
                .exchange().expectStatus().isUnauthorized();
    }
    @Test void validSignedTokenAuthenticatesTheRequest() {
        client().get().uri("/protected").headers(h -> h.setBearerAuth(token(Instant.now().plusSeconds(60))))
                .exchange().expectStatus().isOk();
    }
    @Test void expiredTokenIsRejected() {
        client().get().uri("/protected").headers(h -> h.setBearerAuth(token(Instant.now().minusSeconds(60))))
                .exchange().expectStatus().isUnauthorized();
    }
    @SpringBootConfiguration
    @EnableAutoConfiguration(excludeName = "org.springframework.cloud.gateway.config.GatewayRedisAutoConfiguration")
    static class TestApp {
        @Bean RouterFunction<ServerResponse> routes() {
            return RouterFunctions.route().GET("/protected", r -> ServerResponse.ok().bodyValue("ok"))
                    .POST("/api/v1/auth/login", r -> ServerResponse.ok().bodyValue("ok")).build();
        }
    }
}
