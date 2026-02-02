package se.hrplatform.userservice.model.dto;

import java.util.Set;

public record WorkRoleRequest(
  Long id,
  String name,
  String description,
  Set<UserDto> users
)
{}
