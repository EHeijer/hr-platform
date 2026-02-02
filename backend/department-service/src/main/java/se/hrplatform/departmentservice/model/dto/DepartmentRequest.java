package se.hrplatform.departmentservice.model.dto;

import jakarta.validation.constraints.NotBlank;

public record DepartmentRequest(
        Long id,
        @NotBlank String name,
        @NotBlank String code,
        String description,
        Boolean active
) {}
