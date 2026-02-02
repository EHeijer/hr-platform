package se.hrplatform.userservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.hrplatform.userservice.model.dto.WorkRoleRequest;
import se.hrplatform.userservice.model.dto.WorkRoleResponse;
import se.hrplatform.userservice.service.WorkRoleService;

import java.util.List;

@RestController
@RequestMapping("/api/users/roles")
@RequiredArgsConstructor
@Slf4j
public class WorkRoleController {

  private final WorkRoleService workRoleService;

  @GetMapping
  public ResponseEntity<List<WorkRoleResponse>> getAllWorkRoles() {
    return ResponseEntity.ok(workRoleService.getAllRolesWithUsers());
  }

  @PutMapping("/{id}")
  public ResponseEntity<WorkRoleResponse> updateRole(@PathVariable Long id, @Valid @RequestBody WorkRoleRequest roleRequest) {
    return ResponseEntity.ok(workRoleService.updateRole(id, roleRequest));
  }

  @PostMapping
  public ResponseEntity<WorkRoleResponse> createRole(@Valid @RequestBody WorkRoleRequest roleRequest) {
    return ResponseEntity.ok(workRoleService.createRole(roleRequest));
  }
}
