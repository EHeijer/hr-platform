package se.hrplatform.userservice.model.dto;

public record UserDto(
    String id,
    String name,
    String username,
    String email,
    boolean active,
    Long departmentId
){}
