package se.hrplatform.gateway.model.dto;

import java.util.List;

public record CurrentUserDto(
        String id,
        String username,
        String email,
        List<String> roles
) {}
