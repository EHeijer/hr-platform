package se.hrplatform.departmentservice.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import se.hrplatform.departmentservice.mapper.DepartmentMapper;
import se.hrplatform.departmentservice.model.dto.DepartmentRequest;
import se.hrplatform.departmentservice.model.dto.DepartmentResponse;
import se.hrplatform.departmentservice.model.entity.DepartmentEntity;
import se.hrplatform.departmentservice.repository.DepartmentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentService {

  private final DepartmentRepository repository;

  public DepartmentResponse create(DepartmentRequest req) {

    if (repository.existsByNameIgnoreCase(req.name())) {
      throw new IllegalArgumentException("Department name already exists");
    }

    if (repository.existsByCodeIgnoreCase(req.code())) {
      throw new IllegalArgumentException("Department code already exists");
    }

    DepartmentEntity entity = DepartmentMapper.toEntity(req);
    return DepartmentMapper.toResponse(repository.save(entity));
  }

  public DepartmentResponse update(Long id, DepartmentRequest req) {
    DepartmentEntity entity = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));

    entity.setName(req.name());
    entity.setCode(req.code());
    entity.setDescription(req.description());
    entity.setActive(req.active());

    return DepartmentMapper.toResponse(entity); // JPA dirty checking sparar automatiskt pga @Transactional - så ingen save behövs
  }

  @Transactional(readOnly = true)
  public List<DepartmentResponse> findAll() {
    return repository.findAll()
            .stream()
            .map(DepartmentMapper::toResponse)
            .toList();
  }

  @Transactional(readOnly = true)
  public DepartmentResponse findById(Long id) {
    DepartmentEntity entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Department not found"));

    return DepartmentMapper.toResponse(entity);
  }

  public void delete(Long id) {
    repository.deleteById(id);
  }
}
