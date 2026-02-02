package se.hrplatform.userservice.utils;

import se.hrplatform.userservice.model.dto.KeycloakUserEvent;

public class KeycloakEventTestData {

  public static KeycloakUserEvent event(String type, String userId) {
    return new KeycloakUserEvent(
            type,
            userId,
            "testuser_" + userId,
            "Test User",
            "test_" + userId + "@example.com",
            true,
            System.currentTimeMillis()
    );
  }

  public static KeycloakUserEvent login(String userId) {
    return event("USER_LOGGED_IN", userId);
  }

  public static KeycloakUserEvent created(String userId) {
    return event("USER_CREATED", userId);
  }

  public static KeycloakUserEvent updated(String userId) {
    return event("USER_UPDATED", userId);
  }

  public static KeycloakUserEvent deleted(String userId) {
    return event("USER_DELETED", userId);
  }
}
