package se.hrplatform.gateway.keycloak;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class KeycloakLogoutSuccessHandler
        implements ServerLogoutSuccessHandler {

    @Value("${hr-platform.base-url:http://localhost:8081}")
    private String baseUrl;

    private final ReactiveClientRegistrationRepository registrations;

    public KeycloakLogoutSuccessHandler(
            ReactiveClientRegistrationRepository registrations) {
        this.registrations = registrations;
    }

    @Override
    public Mono<Void> onLogoutSuccess(
            WebFilterExchange exchange,
            Authentication authentication) {


        return registrations.findByRegistrationId("keycloak")
            .flatMap(reg -> {

                String endSessionEndpoint =
                    reg.getProviderDetails()
                        .getConfigurationMetadata()
                        .get("end_session_endpoint")
                        .toString();

                String idToken = ((OidcUser) authentication.getPrincipal())
                    .getIdToken()
                    .getTokenValue();

                String logoutUrl = UriComponentsBuilder
                    .fromUriString(endSessionEndpoint)
                    .queryParam("id_token_hint", idToken)
                    .queryParam("post_logout_redirect_uri", baseUrl)
                    .build()
                    .toUriString();

                exchange.getExchange()
                    .getResponse()
                    .setStatusCode(HttpStatus.FOUND);
                exchange.getExchange()
                    .getResponse()
                    .getHeaders()
                    .setLocation(URI.create(logoutUrl));

                return exchange.getExchange().getResponse().setComplete();
            });
    }
}

