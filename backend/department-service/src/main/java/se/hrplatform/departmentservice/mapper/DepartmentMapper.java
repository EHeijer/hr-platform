package se.hrplatform.departmentservice.mapper;

import se.hrplatform.departmentservice.model.dto.DepartmentRequest;
import se.hrplatform.departmentservice.model.dto.DepartmentResponse;
import se.hrplatform.departmentservice.model.entity.DepartmentEntity;

public class DepartmentMapper {

  public static DepartmentEntity toEntity(DepartmentRequest req) {
    return DepartmentEntity.builder()
            .name(req.name())
            .code(req.code())
            .description(req.description())
            .active(req.active() == null || req.active())
            .build();
  }

  public static DepartmentResponse toResponse(DepartmentEntity e) {
    return new DepartmentResponse(
            e.getId(),
            e.getName(),
            e.getCode(),
            e.getDescription(),
            e.isActive()
    );
  }
}
