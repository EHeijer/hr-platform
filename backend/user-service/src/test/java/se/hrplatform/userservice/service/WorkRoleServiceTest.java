package se.hrplatform.userservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import se.hrplatform.userservice.model.dto.WorkRoleRequest;
import se.hrplatform.userservice.model.dto.WorkRoleResponse;
import se.hrplatform.userservice.model.entity.WorkRoleEntity;
import se.hrplatform.userservice.repository.WorkRoleRepository;

import java.util.Set;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkRoleServiceTest {

  @Mock
  private WorkRoleRepository workRoleRepository;

  @InjectMocks
  private WorkRoleService workRoleService;

  private WorkRoleEntity roleEntity;
  private WorkRoleRequest roleRequest;

  @BeforeEach
  void setup() {
    roleEntity = WorkRoleEntity.builder()
            .id(1L)
            .name("Developer")
            .description("Writes code")
            .users(Set.of())
            .build();

    roleRequest = new WorkRoleRequest(
            1L,
            "Developer Updated",
            "Writes better code",
            Set.of()
    );
  }

  // ----------------------------------------------------------
  // getAllRolesWithUsers
  // ----------------------------------------------------------
  @Test
  void getAllRolesWithUsers_shouldReturnMappedRoles() {
    when(workRoleRepository.findAllWithUsers()).thenReturn(List.of(roleEntity));

    List<WorkRoleResponse> result = workRoleService.getAllRolesWithUsers();

    assertEquals(1, result.size());
    assertEquals("Developer", result.get(0).name());
  }

  @Test
  void getAllRolesWithUsers_shouldReturnEmptyList() {
    when(workRoleRepository.findAllWithUsers()).thenReturn(List.of());

    List<WorkRoleResponse> result = workRoleService.getAllRolesWithUsers();

    assertTrue(result.isEmpty());
  }

  // ----------------------------------------------------------
  // updateRole
  // ----------------------------------------------------------
  @Test
  void updateRole_shouldUpdateFieldsAndReturnResponse() {
    when(workRoleRepository.findById(1L)).thenReturn(Optional.of(roleEntity));

    WorkRoleResponse result = workRoleService.updateRole(1L, roleRequest);

    assertEquals("Developer Updated", result.name());
    assertEquals("Writes better code", result.description());
    verify(workRoleRepository).findById(1L);
  }

  @Test
  void updateRole_shouldThrow404WhenRoleNotFound() {
    when(workRoleRepository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(ResponseStatusException.class, () ->
            workRoleService.updateRole(99L, roleRequest)
    );
  }

  // ----------------------------------------------------------
  // createRole
  // ----------------------------------------------------------
  @Test
  void createRole_shouldCreateNewRole() {
    WorkRoleRequest newRole = new WorkRoleRequest(null, "Tester", "Tests stuff", Set.of());

    when(workRoleRepository.existsByNameIgnoreCase("Tester")).thenReturn(false);
    when(workRoleRepository.save(any())).thenAnswer(inv -> {
      WorkRoleEntity saved = inv.getArgument(0);
      saved.setId(10L);
      return saved;
    });

    WorkRoleResponse result = workRoleService.createRole(newRole);

    assertEquals("Tester", result.name());
    assertEquals("Tests stuff", result.description());
    assertEquals(10L, result.id());
  }

  @Test
  void createRole_shouldThrowWhenNameExists() {
    WorkRoleRequest duplicate = new WorkRoleRequest(null, "Developer", "desc", Set.of());

    when(workRoleRepository.existsByNameIgnoreCase("Developer")).thenReturn(true);

    assertThrows(IllegalArgumentException.class, () ->
            workRoleService.createRole(duplicate)
    );
  }
}

