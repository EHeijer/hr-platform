package se.hrplatform.userservice.mapper;

import org.junit.jupiter.api.Test;
import se.hrplatform.userservice.model.dto.UserDto;
import se.hrplatform.userservice.model.dto.WorkRoleDto;
import se.hrplatform.userservice.model.dto.WorkRoleRequest;
import se.hrplatform.userservice.model.dto.WorkRoleResponse;
import se.hrplatform.userservice.model.entity.UserProfileEntity;
import se.hrplatform.userservice.model.entity.WorkRoleEntity;


import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RoleMapperTest {

  @Test
  void mapToWorkRoleDto_shouldMapAllFields() {
    WorkRoleEntity entity = WorkRoleEntity.builder()
            .id(1L)
            .name("DEV")
            .description("Developer")
            .build();

    WorkRoleDto dto = RoleMapper.mapToWorkRoleDto(entity);

    assertEquals(1L, dto.id());
    assertEquals("DEV", dto.name());
    assertEquals("Developer", dto.description());
  }

  @Test
  void mapToWorkRoleEntity_fromDto_shouldMapAllFields() {
    WorkRoleDto dto = new WorkRoleDto(1L, "DEV", "Developer");

    WorkRoleEntity entity = RoleMapper.mapToWorkRoleEntity(dto);

    assertEquals(1L, entity.getId());
    assertEquals("DEV", entity.getName());
    assertEquals("Developer", entity.getDescription());
    assertNull(entity.getUsers());
  }

  @Test
  void mapToWorkRoleEntity_fromRequestWithoutUsers_shouldMapFieldsAndEmptyUsers() {
    WorkRoleRequest request = new WorkRoleRequest(
            1L,
            "DEV",
            "Developer",
            Set.of()
    );

    WorkRoleEntity entity = RoleMapper.mapToWorkRoleEntity(request, false);

    assertEquals(1L, entity.getId());
    assertEquals("DEV", entity.getName());
    assertEquals("Developer", entity.getDescription());
    assertTrue(entity.getUsers().isEmpty());
  }

  @Test
  void mapToWorkRoleEntity_fromRequestWithUsers_shouldMapUsers() {
    UserDto userDto = new UserDto("u1", "Alice", "alice", "alice@example.com", true, 10L);

    WorkRoleRequest request = new WorkRoleRequest(
            1L,
            "DEV",
            "Developer",
            Set.of(userDto)
    );

    WorkRoleEntity entity = RoleMapper.mapToWorkRoleEntity(request, true);

    assertEquals(1L, entity.getId());
    assertEquals("DEV", entity.getName());
    assertEquals("Developer", entity.getDescription());
    assertEquals(1, entity.getUsers().size());
    assertEquals("u1", entity.getUsers().iterator().next().getId());
  }

  @Test
  void mapToWorkRoleResponse_shouldMapFieldsAndUsers() {
    UserProfileEntity user = UserProfileEntity.builder()
            .id("u1")
            .username("alice")
            .name("Alice")
            .email("alice@example.com")
            .active(true)
            .departmentId(10L)
            .build();

    WorkRoleEntity role = WorkRoleEntity.builder()
            .id(1L)
            .name("DEV")
            .description("Developer")
            .users(Set.of(user))
            .build();

    WorkRoleResponse response = RoleMapper.mapToWorkRoleResponse(role);

    assertEquals(1L, response.id());
    assertEquals("DEV", response.name());
    assertEquals("Developer", response.description());
    assertEquals(1, response.users().size());
    assertEquals("u1", response.users().iterator().next().id());
  }
}

