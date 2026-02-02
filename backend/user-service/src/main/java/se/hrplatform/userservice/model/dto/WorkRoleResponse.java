package se.hrplatform.userservice.model.dto;

import java.util.Set;

public record WorkRoleResponse (
  Long id,
  String name,
  String description,
  Set<UserDto> users
)
{}
