package se.hrplatform.departmentservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import se.hrplatform.departmentservice.filter.GatewayHeaderAuthFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Value("${security.gateway.hmac-secret}")
  private String secret;

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
      .authorizeHttpRequests(auth -> auth
              .requestMatchers(HttpMethod.GET, "/api/departments/**").hasAnyRole("ADMIN", "HR", "EMPLOYEE")
              .requestMatchers(HttpMethod.POST, "/api/departments/**").hasAnyRole("ADMIN", "HR")
              .requestMatchers(HttpMethod.PUT, "/api/departments/**").hasAnyRole("ADMIN", "HR")
              .requestMatchers(HttpMethod.DELETE, "/api/departments/**").hasAnyRole("ADMIN", "HR")
              .anyRequest().denyAll())
      .addFilterBefore(new GatewayHeaderAuthFilter(secret), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
