package se.hrplatform.userservice.model.dto;

import se.hrplatform.userservice.model.entity.WorkRoleEntity;

import java.util.Set;

public record UserResponse(
    String id,
    String name,
    String username,
    String email,
    boolean active,
    Set<WorkRoleDto> roles,
    Long departmentId
){}
