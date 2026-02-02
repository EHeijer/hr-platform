package se.hrplatform.departmentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.hrplatform.departmentservice.model.entity.DepartmentEntity;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {

  Optional<DepartmentEntity> findByCode(String code);

  boolean existsByNameIgnoreCase(String name);

  boolean existsByCodeIgnoreCase(String code);
}
