package se.hrplatform.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.hrplatform.userservice.model.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {
}

