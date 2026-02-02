package se.hrplatform.userservice.mapper;

import se.hrplatform.userservice.model.dto.UserDto;
import se.hrplatform.userservice.model.dto.WorkRoleDto;
import se.hrplatform.userservice.model.dto.WorkRoleRequest;
import se.hrplatform.userservice.model.dto.WorkRoleResponse;
import se.hrplatform.userservice.model.entity.UserProfileEntity;
import se.hrplatform.userservice.model.entity.WorkRoleEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class RoleMapper {

  public static WorkRoleDto mapToWorkRoleDto(WorkRoleEntity workRoleEntity) {
    return new WorkRoleDto(
            workRoleEntity.getId(),
            workRoleEntity.getName(),
            workRoleEntity.getDescription()
    );
  }

  public static WorkRoleEntity mapToWorkRoleEntity(WorkRoleDto workRoleDto) {
    return new WorkRoleEntity(
            workRoleDto.id(),
            workRoleDto.name(),
            workRoleDto.description(),
            null
    );
  }

  public static WorkRoleEntity mapToWorkRoleEntity(WorkRoleRequest workRoleRequest, boolean withUsers) {
    Set<UserProfileEntity> users = new HashSet<>();
    if(withUsers) {
       users = workRoleRequest.users()
              .stream()
              .map(UserMapper::mapToUserProfileEntity)
              .collect(Collectors.toSet());
    }

    return new WorkRoleEntity(
            workRoleRequest.id(),
            workRoleRequest.name(),
            workRoleRequest.description(),
            users
    );
  }

  public static WorkRoleResponse mapToWorkRoleResponse(WorkRoleEntity workRoleEntity) {
    Set<UserDto> userDtos = workRoleEntity.getUsers()
            .stream()
            .map(UserMapper::mapToUserDto)
            .collect(Collectors.toSet());
    return new WorkRoleResponse(
            workRoleEntity.getId(),
            workRoleEntity.getName(),
            workRoleEntity.getDescription(),
            userDtos
    );
  }
}
