package se.hrplatform.userservice.mapper;

import org.junit.jupiter.api.Test;
import se.hrplatform.userservice.model.dto.*;
import se.hrplatform.userservice.model.entity.UserProfileEntity;
import se.hrplatform.userservice.model.entity.WorkRoleEntity;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

  @Test
  void mapToUserProfileEntity_fromKeycloakEvent_shouldMapAllFields() {
    KeycloakUserEvent event = new KeycloakUserEvent(
            "USER_CREATED",
            "u1",
            "alice",
            "Alice",
            "alice@example.com",
            true,
            System.currentTimeMillis()
    );

    UserProfileEntity result = UserMapper.mapToUserProfileEntity(event);

    assertEquals("u1", result.getId());
    assertEquals("alice", result.getUsername());
    assertEquals("Alice", result.getName());
    assertEquals("alice@example.com", result.getEmail());
    assertTrue(result.isActive());
  }

  @Test
  void mapToUserProfileEntity_fromUserRequest_shouldMapFieldsAndRoles() {
    WorkRoleDto roleDto = new WorkRoleDto(1L, "DEV", "Developer");
    UserRequest request = new UserRequest(
            "u1",
            "Alice",
            "alice",
            "alice@example.com",
            true,
            Set.of(roleDto)
    );

    UserProfileEntity result = UserMapper.mapToUserProfileEntity(request);

    assertEquals("u1", result.getId());
    assertEquals("alice", result.getUsername());
    assertEquals("Alice", result.getName());
    assertEquals("alice@example.com", result.getEmail());
    assertTrue(result.isActive());
    assertEquals(1, result.getRoles().size());
    assertEquals("DEV", result.getRoles().iterator().next().getName());
  }

  @Test
  void mapToUserProfileEntity_fromUserRequest_shouldHandleEmptyRoles() {
    UserRequest request = new UserRequest(
            "u1",
            "Alice",
            "alice",
            "alice@example.com",
            true,
            Set.of()
    );

    UserProfileEntity result = UserMapper.mapToUserProfileEntity(request);

    assertNotNull(result.getRoles());
    assertTrue(result.getRoles().isEmpty());
  }

  @Test
  void mapToUserProfileEntity_fromUserDto_shouldMapFields() {
    UserDto dto = new UserDto(
            "u1",
            "Alice",
            "alice",
            "alice@example.com",
            true,
            10L
    );

    UserProfileEntity result = UserMapper.mapToUserProfileEntity(dto);

    assertEquals("u1", result.getId());
    assertEquals("alice", result.getUsername());
    assertEquals("Alice", result.getName());
    assertEquals("alice@example.com", result.getEmail());
    assertTrue(result.isActive());
    assertEquals(10L, result.getDepartmentId());
  }

  @Test
  void mapToUserResponse_withRolesTrue_shouldMapRoles() {
    WorkRoleEntity role = WorkRoleEntity.builder()
            .id(1L)
            .name("DEV")
            .description("Developer")
            .build();

    UserProfileEntity entity = UserProfileEntity.builder()
            .id("u1")
            .username("alice")
            .name("Alice")
            .email("alice@example.com")
            .active(true)
            .departmentId(10L)
            .roles(Set.of(role))
            .build();

    UserResponse result = UserMapper.mapToUserResponse(entity, true);

    assertEquals("u1", result.id());
    assertEquals("Alice", result.name());
    assertEquals("alice", result.username());
    assertEquals("alice@example.com", result.email());
    assertTrue(result.active());
    assertEquals(1, result.roles().size());
  }

  @Test
  void mapToUserResponse_withRolesFalse_shouldNotIncludeRoles() {
    UserProfileEntity entity = UserProfileEntity.builder()
            .id("u1")
            .username("alice")
            .name("Alice")
            .email("alice@example.com")
            .active(true)
            .departmentId(10L)
            .roles(Set.of(
                    WorkRoleEntity.builder().id(1L).name("DEV").build()
            ))
            .build();

    UserResponse result = UserMapper.mapToUserResponse(entity, false);

    assertNull(result.roles());
  }

  @Test
  void mapToUserDto_shouldMapAllFields() {
    UserProfileEntity entity = UserProfileEntity.builder()
            .id("u1")
            .username("alice")
            .name("Alice")
            .email("alice@example.com")
            .active(true)
            .departmentId(10L)
            .build();

    UserDto dto = UserMapper.mapToUserDto(entity);

    assertEquals("u1", dto.id());
    assertEquals("Alice", dto.name());
    assertEquals("alice", dto.username());
    assertEquals("alice@example.com", dto.email());
    assertTrue(dto.active());
    assertEquals(10L, dto.departmentId());
  }
}

