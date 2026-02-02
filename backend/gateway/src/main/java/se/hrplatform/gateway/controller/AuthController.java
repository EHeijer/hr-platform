package se.hrplatform.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import se.hrplatform.gateway.model.dto.CurrentUserDto;
import se.hrplatform.gateway.service.AuthService;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/current-user")
    public Mono<CurrentUserDto> currentUser(@AuthenticationPrincipal OAuth2User user) {
        return Mono.just(authService.getCurrentUser(user));
    }
}

