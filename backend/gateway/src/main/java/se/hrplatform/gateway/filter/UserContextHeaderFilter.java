package se.hrplatform.gateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.stream.Collectors;

@Component
public class UserContextHeaderFilter implements GlobalFilter {

    public static final String X_USER = "X-User";
    public static final String X_ROLES = "X-Roles";
    public static final String X_TIMESTAMP = "X-Timestamp";
    public static final String X_SIGNATURE = "X-Signature";

    @Value("${security.gateway.hmac-secret}")
    private String secret;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        return exchange.getPrincipal()
                .cast(Authentication.class)
                .flatMap(auth -> {

                    String user = auth.getName();
                    String roles = auth.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.joining(","));

                    String timestamp = String.valueOf(System.currentTimeMillis());

                    String payload = user + "|" + roles + "|" + timestamp;
                    String signature = hmacSha256(payload, secret);

                    ServerHttpRequest request = exchange.getRequest().mutate()
                            .header(X_USER, user)
                            .header(X_ROLES, roles)
                            .header(X_TIMESTAMP, timestamp)
                            .header(X_SIGNATURE, signature)
                            .build();

                    return chain.filter(exchange.mutate().request(request).build());
                })
                .switchIfEmpty(chain.filter(exchange));
    }

    private String hmacSha256(String data, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec key =
                    new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(key);
            return Base64.getEncoder().encodeToString(mac.doFinal(data.getBytes()));
        } catch (Exception e) {
            throw new IllegalStateException("HMAC error", e);
        }
    }
}
