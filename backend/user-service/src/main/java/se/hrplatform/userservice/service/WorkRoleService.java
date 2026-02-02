package se.hrplatform.userservice.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import se.hrplatform.userservice.mapper.RoleMapper;
import se.hrplatform.userservice.model.dto.WorkRoleRequest;
import se.hrplatform.userservice.model.dto.WorkRoleResponse;
import se.hrplatform.userservice.model.entity.WorkRoleEntity;
import se.hrplatform.userservice.repository.WorkRoleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class WorkRoleService {

  private final WorkRoleRepository workRoleRepository;

  @Transactional(readOnly = true)
  public List<WorkRoleResponse> getAllRolesWithUsers() {
    return workRoleRepository.findAllWithUsers()
            .stream()
            .map(RoleMapper::mapToWorkRoleResponse)
            .collect(Collectors.toList());
  }

  public WorkRoleResponse updateRole(Long id, WorkRoleRequest roleRequest) {
    WorkRoleEntity workRoleEntity = workRoleRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));

    workRoleEntity.setName(roleRequest.name());
    workRoleEntity.setDescription(roleRequest.description());
    // TODO: om man ska kunna lägga till användare på en roll behöver vi lägga till det här. Alternativt uppdatera från user-hållet istället ?

    return RoleMapper.mapToWorkRoleResponse(workRoleEntity); // JPA dirty checking sparar automatiskt pga @Transactional - så ingen save behövs
  }

  public WorkRoleResponse createRole(WorkRoleRequest roleRequest) {
    if (workRoleRepository.existsByNameIgnoreCase(roleRequest.name())) {
      throw new IllegalArgumentException("Role name already exists");
    }

    WorkRoleEntity workRoleEntity = workRoleRepository.save(RoleMapper.mapToWorkRoleEntity(roleRequest, false));
    return RoleMapper.mapToWorkRoleResponse(workRoleEntity);
  }
}
