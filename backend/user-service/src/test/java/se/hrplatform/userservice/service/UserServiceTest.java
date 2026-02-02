package se.hrplatform.userservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import se.hrplatform.userservice.model.dto.UserRequest;
import se.hrplatform.userservice.model.dto.UserResponse;
import se.hrplatform.userservice.model.entity.UserProfileEntity;
import se.hrplatform.userservice.repository.UserProfileRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserProfileRepository userProfileRepository;

  @InjectMocks
  private UserService userService;

  private UserProfileEntity userEntity;
  private UserRequest userRequest;

  @BeforeEach
  void setup() {
    userEntity = UserProfileEntity.builder()
            .id("u1")
            .username("alice")
            .name("Alice")
            .email("alice@example.com")
            .active(true)
            .departmentId(10L)
            .roles(Set.of())
            .build();

    userRequest = new UserRequest(
            "u1",
            "Alice Updated",
            "alice_new",
            "alice_new@example.com",
            true,
            Set.of()
    );
  }

  // ----------------------------------------------------------
  // getAllUsersAndRoles
  // ----------------------------------------------------------
  @Test
  void getAllUsersAndRoles_shouldReturnMappedUsers() {
    when(userProfileRepository.findAllWithRoles()).thenReturn(List.of(userEntity));

    List<UserResponse> result = userService.getAllUsersAndRoles();

    assertEquals(1, result.size());
    assertEquals("u1", result.get(0).id());
    verify(userProfileRepository).findAllWithRoles();
  }

  @Test
  void getAllUsersAndRoles_shouldReturnEmptyListWhenNoUsers() {
    when(userProfileRepository.findAllWithRoles()).thenReturn(List.of());

    List<UserResponse> result = userService.getAllUsersAndRoles();

    assertTrue(result.isEmpty());
  }

  // ----------------------------------------------------------
  // getUserProfileById
  // ----------------------------------------------------------
  @Test
  void getUserProfileById_shouldReturnUser() {
    when(userProfileRepository.findByIdWithRoles("u1")).thenReturn(Optional.of(userEntity));

    Optional<UserResponse> result = userService.getUserProfileById("u1");

    assertTrue(result.isPresent());
    assertEquals("u1", result.get().id());
  }

  @Test
  void getUserProfileById_shouldReturnEmptyOptionalWhenNotFound() {
    when(userProfileRepository.findByIdWithRoles("missing")).thenReturn(Optional.empty());

    Optional<UserResponse> result = userService.getUserProfileById("missing");

    assertTrue(result.isEmpty());
  }

  // ----------------------------------------------------------
  // updateUserProfile
  // ----------------------------------------------------------
  @Test
  void updateUserProfile_shouldUpdateFieldsAndReturnResponse() {
    when(userProfileRepository.findById("u1")).thenReturn(Optional.of(userEntity));

    UserResponse result = userService.updateUserProfile("u1", userRequest);

    assertEquals("Alice Updated", result.name());
    assertEquals("alice_new", result.username());
    assertEquals("alice_new@example.com", result.email());
    verify(userProfileRepository).findById("u1");
  }

  @Test
  void updateUserProfile_shouldThrow404WhenUserNotFound() {
    when(userProfileRepository.findById("missing")).thenReturn(Optional.empty());

    assertThrows(ResponseStatusException.class, () ->
            userService.updateUserProfile("missing", userRequest)
    );
  }

  // ----------------------------------------------------------
  // getAllUsersByDepartment
  // ----------------------------------------------------------
  @Test
  void getAllUsersByDepartment_shouldReturnUsers() {
    when(userProfileRepository.findByDepartmentId(10L)).thenReturn(List.of(userEntity));

    List<UserResponse> result = userService.getAllUsersByDepartment(10L);

    assertEquals(1, result.size());
    assertEquals("u1", result.get(0).id());
  }

  @Test
  void getAllUsersByDepartment_shouldReturnEmptyListWhenNoUsers() {
    when(userProfileRepository.findByDepartmentId(99L)).thenReturn(List.of());

    List<UserResponse> result = userService.getAllUsersByDepartment(99L);

    assertTrue(result.isEmpty());
  }
}

