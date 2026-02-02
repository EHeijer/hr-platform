package se.hrplatform.gateway.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import se.hrplatform.gateway.model.dto.CurrentUserDto;

import java.util.List;

@Service
public class AuthService {

    public CurrentUserDto getCurrentUser(OAuth2User user) {

        String username = user.getAttribute("preferred_username");
        String email = user.getAttribute("email");
        String userId = user.getAttribute("sub");

        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return new CurrentUserDto(userId, username, email, roles);
    }
}
