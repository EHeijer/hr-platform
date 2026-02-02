package se.hrplatform.userservice.utils;

import se.hrplatform.userservice.model.entity.UserProfileEntity;

public class UserProfileEntityTestData {

  public static UserProfileEntity createUserProfileEntity(String userId) {
    return UserProfileEntity.builder()
            .id(userId)
            .name("Kalle Karlsson")
            .username("calle")
            .active(true)
            .email("calle@example.com")
            .build();
  }
}
