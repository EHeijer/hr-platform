package se.hrplatform.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.hrplatform.userservice.model.entity.UserProfileEntity;

import java.util.List;
import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfileEntity, String> {

  @Query("select u from UserProfileEntity u join u.roles r where r.name = :roleName")
  List<UserProfileEntity> findUsersByRoleName(@Param("roleName") String roleName);

  @Query("""
        select distinct user
        from UserProfileEntity user
        left join fetch user.roles
    """)
  List<UserProfileEntity> findAllWithRoles();

  @Query("select user from UserProfileEntity user left join fetch user.roles where user.id = :id")
  Optional<UserProfileEntity> findByIdWithRoles(String id);

  List<UserProfileEntity> findByDepartmentId(Long departmentId);

}

