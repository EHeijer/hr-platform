package se.hrplatform.userservice.model.dto;

import java.util.Set;

public record UserRequest(
    String id,
    String name,
    String username,
    String email,
    boolean active,
    Set<WorkRoleDto> roles
){}
