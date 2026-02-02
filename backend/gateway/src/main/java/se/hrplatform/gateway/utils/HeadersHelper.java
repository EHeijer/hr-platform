package se.hrplatform.gateway.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import se.hrplatform.gateway.filter.UserContextHeaderFilter;

import java.util.function.Consumer;

public class HeadersHelper {

    public static Consumer<HttpHeaders> forwardUserContext(ServerHttpRequest req) {
        return h -> {
            h.add(UserContextHeaderFilter.X_USER, req.getHeaders().getFirst(UserContextHeaderFilter.X_USER));
            h.add(UserContextHeaderFilter.X_ROLES, req.getHeaders().getFirst(UserContextHeaderFilter.X_ROLES));
            h.add(UserContextHeaderFilter.X_SIGNATURE, req.getHeaders().getFirst(UserContextHeaderFilter.X_SIGNATURE));
            h.add(UserContextHeaderFilter.X_TIMESTAMP, req.getHeaders().getFirst(UserContextHeaderFilter.X_TIMESTAMP));
        };
    }
}
