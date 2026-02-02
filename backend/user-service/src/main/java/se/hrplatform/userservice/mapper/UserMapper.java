package se.hrplatform.userservice.mapper;

import se.hrplatform.userservice.model.dto.*;
import se.hrplatform.userservice.model.entity.UserProfileEntity;
import se.hrplatform.userservice.model.entity.WorkRoleEntity;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserProfileEntity mapToUserProfileEntity(KeycloakUserEvent event) {
        return UserProfileEntity.builder()
                .id(event.userId())
                .username(event.username())
                .name(event.name())
                .email(event.email())
                .active(event.enabled())
                .build();
    }

    public static UserProfileEntity mapToUserProfileEntity(UserRequest userRequest) {
        Set<WorkRoleEntity> roleEntities = userRequest.roles()
                .stream()
                .map(RoleMapper::mapToWorkRoleEntity)
                .collect(Collectors.toSet());
        return UserProfileEntity.builder()
                .id(userRequest.id())
                .username(userRequest.username())
                .name(userRequest.name())
                .email(userRequest.email())
                .active(userRequest.active())
                .roles(roleEntities)
                .build();
    }

    public static UserProfileEntity mapToUserProfileEntity(UserDto userDto) {

        return UserProfileEntity.builder()
                .id(userDto.id())
                .username(userDto.username())
                .name(userDto.name())
                .email(userDto.email())
                .active(userDto.active())
                .departmentId(userDto.departmentId())
                .build();
    }

    public static UserResponse mapToUserResponse(UserProfileEntity userProfileEntity, boolean withRoles) {

        Set<WorkRoleDto> workRoleDtos = null;
        if(withRoles) {
            workRoleDtos = userProfileEntity.getRoles()
                    .stream()
                    .map(RoleMapper::mapToWorkRoleDto)
                    .collect(Collectors.toSet());
        }
        return new UserResponse(
                userProfileEntity.getId(),
                userProfileEntity.getName(),
                userProfileEntity.getUsername(),
                userProfileEntity.getEmail(),
                userProfileEntity.isActive(),
                workRoleDtos,
                userProfileEntity.getDepartmentId()
        );
    }

    public static UserDto mapToUserDto(UserProfileEntity userProfileEntity) {
        return new UserDto(
                userProfileEntity.getId(),
                userProfileEntity.getName(),
                userProfileEntity.getUsername(),
                userProfileEntity.getEmail(),
                userProfileEntity.isActive(),
                userProfileEntity.getDepartmentId()
        );
    }
}
