package se.hrplatform.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import se.hrplatform.userservice.mapper.UserMapper;
import se.hrplatform.userservice.model.dto.UserRequest;
import se.hrplatform.userservice.model.dto.UserResponse;
import se.hrplatform.userservice.model.entity.UserProfileEntity;
import se.hrplatform.userservice.repository.UserProfileRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

  private final UserProfileRepository userProfileRepository;

  // TODO: använd paginering istället
  @Transactional(readOnly = true)
  public List<UserResponse> getAllUsersAndRoles(){
    return userProfileRepository.findAllWithRoles()
            .stream()
            .map(userProfileEntity -> UserMapper.mapToUserResponse(userProfileEntity, true))
            .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public Optional<UserResponse> getUserProfileById(String userId) {
    return userProfileRepository.findByIdWithRoles(userId)
            .map(userProfileEntity -> UserMapper.mapToUserResponse(userProfileEntity, true));
  }

  public UserResponse updateUserProfile(String userId, UserRequest userRequest) {
    UserProfileEntity userProfileEntity = userProfileRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));

    userProfileEntity.setName(userRequest.name());
    userProfileEntity.setUsername(userRequest.username());
    userProfileEntity.setEmail(userRequest.email());
    userProfileEntity.setActive(userRequest.active());
    // TODO: om man ska kunna lägga till roll på en användare behöver vi mappa det här. Alternativt uppdatera från role-hållet istället ?

    log.info("User updated: {}", userProfileEntity);
    return UserMapper.mapToUserResponse(userProfileEntity, true);
  }

  @Transactional(readOnly = true)
  public List<UserResponse> getAllUsersByDepartment(Long id) {
    return userProfileRepository.findByDepartmentId(id)
            .stream()
            .map(userProfileEntity -> UserMapper.mapToUserResponse(userProfileEntity, false))
            .toList();
  }
}
