package se.hrplatform.userservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.hrplatform.userservice.model.dto.UserRequest;
import se.hrplatform.userservice.model.dto.UserResponse;
import se.hrplatform.userservice.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

  private final UserService userService;

  @GetMapping
  public ResponseEntity<List<UserResponse>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsersAndRoles());
  }

  @GetMapping("/profile/{userId}")
  public ResponseEntity<UserResponse> getUserProfile(@PathVariable String userId){
    return userService.getUserProfileById(userId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/profile/{userId}")
  public ResponseEntity<UserResponse> updateUserProfile(@PathVariable String userId, @Valid @RequestBody UserRequest userRequest) {
    return ResponseEntity.ok(userService.updateUserProfile(userId, userRequest));
  }

  @GetMapping("/departments/{departmentId}")
  public ResponseEntity<List<UserResponse>> getUsersByDepartment(@PathVariable Long departmentId) {
    return ResponseEntity.ok(userService.getAllUsersByDepartment(departmentId));
  }
}
