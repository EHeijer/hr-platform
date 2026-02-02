package se.hrplatform.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import se.hrplatform.userservice.model.dto.UserRequest;
import se.hrplatform.userservice.model.entity.UserProfileEntity;
import se.hrplatform.userservice.model.entity.WorkRoleEntity;
import se.hrplatform.userservice.repository.UserProfileRepository;
import se.hrplatform.userservice.repository.WorkRoleRepository;
import se.hrplatform.userservice.utils.HeaderUtils;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class UserControllerMockMvcIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserProfileRepository userProfileRepository;

  @Autowired
  private WorkRoleRepository workRoleRepository;

  @Autowired
  private ObjectMapper objectMapper;

  private WorkRoleEntity roleDev;
  private UserProfileEntity user1;
  private UserProfileEntity user2;

  @BeforeEach
  void setup() {
    userProfileRepository.deleteAll();
    workRoleRepository.deleteAll();

    roleDev = workRoleRepository.save(
            WorkRoleEntity.builder()
                    .name("DEVELOPER")
                    .description("Writes code")
                    .build()
    );

    user1 = userProfileRepository.save(
            UserProfileEntity.builder()
                    .id("u1")
                    .username("alice")
                    .name("Alice")
                    .email("alice@example.com")
                    .active(true)
                    .roles(Set.of(roleDev))
                    .departmentId(10L)
                    .build()
    );

    user2 = userProfileRepository.save(
            UserProfileEntity.builder()
                    .id("u2")
                    .username("bob")
                    .name("Bob")
                    .email("bob@example.com")
                    .active(true)
                    .roles(Set.of())
                    .departmentId(20L)
                    .build()
    );
  }

  // ----------------------------------------------------------
  // GET /api/users
  // ----------------------------------------------------------
  @Test
  void shouldReturnAllUsersWithRoles() throws Exception {
    mockMvc.perform(get("/api/users")
                    .headers(HeaderUtils.createHeadersWithRolesForMockMvc("ROLE_EMPLOYEE")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].roles").isArray());
  }

  // ----------------------------------------------------------
  // GET /api/users/profile/{id}
  // ----------------------------------------------------------
  @Test
  void shouldReturnUserProfileById() throws Exception {
    mockMvc.perform(get("/api/users/profile/{id}", "u1")
                    .headers(HeaderUtils.createHeadersWithRolesForMockMvc("ROLE_EMPLOYEE")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("u1"))
            .andExpect(jsonPath("$.roles.length()").value(1));
  }

  @Test
  void shouldReturn404WhenUserNotFound() throws Exception {
    mockMvc.perform(get("/api/users/profile/{id}", "missing")
                    .headers(HeaderUtils.createHeadersWithRolesForMockMvc("ROLE_EMPLOYEE")))
            .andExpect(status().isNotFound());
  }

  // ----------------------------------------------------------
  // PUT /api/users/profile/{id}
  // ----------------------------------------------------------
  @Test
  void shouldUpdateUserProfile() throws Exception {
    UserRequest updateRequest = new UserRequest(
            "u1",
            "Alice Updated",
            "alice_new",
            "alice_new@example.com",
            true,
            Set.of()
    );

    mockMvc.perform(put("/api/users/profile/{id}", "u1")
                    .headers(HeaderUtils.createHeadersWithRolesForMockMvc("ROLE_EMPLOYEE"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Alice Updated"))
            .andExpect(jsonPath("$.username").value("alice_new"))
            .andExpect(jsonPath("$.email").value("alice_new@example.com"));
  }

  @Test
  void shouldReturn404WhenUpdatingMissingUser() throws Exception {
    UserRequest updateRequest = new UserRequest(
            "missing",
            "Name",
            "user",
            "email@example.com",
            true,
            Set.of()
    );

    mockMvc.perform(put("/api/users/profile/{id}", "missing")
                    .headers(HeaderUtils.createHeadersWithRolesForMockMvc("ROLE_EMPLOYEE"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isNotFound());
  }

  // ----------------------------------------------------------
  // GET /api/users/departments/{departmentId}
  // ----------------------------------------------------------
  @Test
  void shouldReturnUsersByDepartment() throws Exception {
    mockMvc.perform(get("/api/users/departments/{id}", 10L)
                    .headers(HeaderUtils.createHeadersWithRolesForMockMvc("ROLE_EMPLOYEE")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value("u1"));
  }
}
