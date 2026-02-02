package se.hrplatform.userservice.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import se.hrplatform.userservice.model.dto.UserRequest;
import se.hrplatform.userservice.model.entity.UserProfileEntity;
import se.hrplatform.userservice.model.entity.WorkRoleEntity;
import se.hrplatform.userservice.repository.UserProfileRepository;
import se.hrplatform.userservice.repository.WorkRoleRepository;
import se.hrplatform.userservice.utils.HeaderUtils;

import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerRestAssuredIT {

  @LocalServerPort
  private int port;

  @Autowired
  private UserProfileRepository userProfileRepository;

  @Autowired
  private WorkRoleRepository workRoleRepository;

  private WorkRoleEntity roleDev;
  private UserProfileEntity user1;
  private UserProfileEntity user2;

  @BeforeAll
  void setupRestAssured() {
    RestAssured.baseURI = "http://localhost";
  }

  @BeforeEach
  void setup() {
    RestAssured.port = port;

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
  void getUsers_shouldReturnAllUsers() {
    given()
            .when()
            .headers(HeaderUtils.createHeadersWithRolesForRestAssured("ROLE_EMPLOYEE"))
            .get("/api/users")
            .then()
            .statusCode(200)
            .body("size()", is(2))
            .body("[0].roles", notNullValue());
  }

  @Test
  void getUsers_shouldReturn403WithNoAcceptedRole() {
    given()
            .when()
            .headers(HeaderUtils.createHeadersWithRolesForRestAssured("ROLE_NOT_ACCEPTED"))
            .get("/api/users")
            .then()
            .statusCode(403);
  }

  // ----------------------------------------------------------
  // GET /api/users/profile/{id}
  // ----------------------------------------------------------
  @Test
  void getUserProfile_shouldReturnUserProfile() {
    given()
            .when()
            .headers(HeaderUtils.createHeadersWithRolesForRestAssured("ROLE_ADMIN,ROLE_EMPLOYEE"))
            .get("/api/users/profile/u1")
            .then()
            .statusCode(200)
            .body("id", equalTo("u1"))
            .body("roles.size()", is(1));
  }

  @Test
  void getUserProfile_shouldReturn404ForMissingUser() {
    given()
            .when()
            .headers(HeaderUtils.createHeadersWithRolesForRestAssured("ROLE_ADMIN,ROLE_EMPLOYEE"))
            .get("/api/users/profile/missing")
            .then()
            .statusCode(404);
  }

  @Test
  void getUserProfile_shouldReturn403WithNoAcceptedRole() {
    given()
            .when()
            .headers(HeaderUtils.createHeadersWithRolesForRestAssured("ROLE_NOT_ACCEPTED"))
            .get("/api/users/profile/u1")
            .then()
            .statusCode(403);
  }

  // ----------------------------------------------------------
  // PUT /api/users/profile/{id}
  // ----------------------------------------------------------
  @Test
  void shouldUpdateUserProfile() {
    UserRequest update = new UserRequest(
            "u1",
            "Alice Updated",
            "alice_new",
            "alice_new@example.com",
            true,
            Set.of()
    );

    given()
            .contentType(ContentType.JSON)
            .headers(HeaderUtils.createHeadersWithRolesForRestAssured("ROLE_ADMIN,ROLE_EMPLOYEE"))
            .body(update)
            .when()
            .put("/api/users/profile/u1")
            .then()
            .statusCode(200)
            .body("name", equalTo("Alice Updated"))
            .body("username", equalTo("alice_new"))
            .body("email", equalTo("alice_new@example.com"));
  }

  // ----------------------------------------------------------
  // GET /api/users/departments/{id}
  // ----------------------------------------------------------
  @Test
  void shouldReturnUsersByDepartment() {
    given()
            .when()
            .headers(HeaderUtils.createHeadersWithRolesForRestAssured("ROLE_ADMIN,ROLE_EMPLOYEE"))
            .get("/api/users/departments/10")
            .then()
            .statusCode(200)
            .body("size()", is(1))
            .body("[0].id", equalTo("u1"));
  }
}

