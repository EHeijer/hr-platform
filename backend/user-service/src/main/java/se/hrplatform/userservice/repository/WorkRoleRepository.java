package se.hrplatform.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.hrplatform.userservice.model.entity.UserProfileEntity;
import se.hrplatform.userservice.model.entity.WorkRoleEntity;

import java.util.List;
import java.util.Optional;

public interface WorkRoleRepository extends JpaRepository<WorkRoleEntity, Long> {

  Optional<WorkRoleEntity> findByName(String name);

  @Query("""
        select distinct role
        from WorkRoleEntity role
        left join fetch role.users
    """)
  List<WorkRoleEntity> findAllWithUsers();

  boolean existsByNameIgnoreCase(String name);

}

