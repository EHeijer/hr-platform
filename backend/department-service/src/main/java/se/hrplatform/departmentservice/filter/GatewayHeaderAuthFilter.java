package se.hrplatform.departmentservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class GatewayHeaderAuthFilter extends OncePerRequestFilter {

    private final String secret;

    public GatewayHeaderAuthFilter(String secret) {
        this.secret = secret;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String user = request.getHeader("X-User");
        String roles = request.getHeader("X-Roles");
        String timestamp = request.getHeader("X-Timestamp");
        String signature = request.getHeader("X-Signature");

        if (user == null || roles == null || timestamp == null || signature == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String payload = user + "|" + roles + "|" + timestamp;
        String expected = hmacSha256(payload, secret);

        if (!MessageDigest.isEqual(expected.getBytes(), signature.getBytes())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // (Valfritt) skydd mot replay attacks
        long ts = Long.parseLong(timestamp);
        if (Math.abs(System.currentTimeMillis() - ts) > 60_000) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        List<SimpleGrantedAuthority> authorities =
                Arrays.stream(roles.split(","))
                        .map(String::trim)
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(user, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
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


