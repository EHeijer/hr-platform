package se.hrplatform.gateway.keycloak;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
public class KeycloakReactiveOAuth2UserService
        implements ReactiveOAuth2UserService<OidcUserRequest, OidcUser> {

    private final OidcReactiveOAuth2UserService delegate =
            new OidcReactiveOAuth2UserService();

    @Override
    public Mono<OidcUser> loadUser(OidcUserRequest userRequest) {

        return delegate.loadUser(userRequest)
                .map(oidcUser -> {

                    List<GrantedAuthority> authorities = new ArrayList<>();

                    List<String> roles =
                            oidcUser.getIdToken().getClaimAsStringList("roles");

                    if (roles != null) {
                        roles.forEach(role ->
                                authorities.add(
                                        new SimpleGrantedAuthority("ROLE_" + role)
                                )
                        );
                    }

                    return new DefaultOidcUser(
                            authorities,
                            oidcUser.getIdToken(),
                            oidcUser.getUserInfo(),
                            "preferred_username"
                    );
                });
    }
}

