package se.hrplatform.userservice.model.dto;

import java.util.Set;

public record KeycloakUserEvent (
    String type, // USER_CREATED, USER_UPDATED, USER_DELETED, USER_LOGGED_IN
    String userId,
    String username,
    String name,
    String email,
    boolean enabled,
    long timestamp
){}

