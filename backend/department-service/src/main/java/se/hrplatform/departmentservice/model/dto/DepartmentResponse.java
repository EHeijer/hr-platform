package se.hrplatform.departmentservice.model.dto;

public record DepartmentResponse(
        Long id,
        String name,
        String code,
        String description,
        boolean active
) {}
