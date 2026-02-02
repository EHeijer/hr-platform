package se.hrplatform.departmentservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.hrplatform.departmentservice.model.dto.DepartmentRequest;
import se.hrplatform.departmentservice.model.dto.DepartmentResponse;
import se.hrplatform.departmentservice.service.DepartmentService;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@Slf4j
public class DepartmentController {

  private final DepartmentService service;

  @PostMapping
  public ResponseEntity<DepartmentResponse> create(@Valid @RequestBody DepartmentRequest req) {
    return ResponseEntity.ok(service.create(req));
  }

  @PutMapping("/{id}")
  public ResponseEntity<DepartmentResponse> update(
          @PathVariable Long id,
          @Valid @RequestBody DepartmentRequest req
  ) {
    return ResponseEntity.ok(service.update(id, req));
  }

  @GetMapping
  public ResponseEntity<List<DepartmentResponse>> getAll() {
    return ResponseEntity.ok(service.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<DepartmentResponse> getById(@PathVariable Long id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
