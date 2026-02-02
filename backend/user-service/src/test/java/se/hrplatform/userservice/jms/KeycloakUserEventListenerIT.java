package se.hrplatform.userservice.jms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;
import se.hrplatform.userservice.config.JmsTestConfig;
import se.hrplatform.userservice.model.dto.KeycloakUserEvent;
import se.hrplatform.userservice.model.entity.UserProfileEntity;
import se.hrplatform.userservice.repository.UserProfileRepository;
import se.hrplatform.userservice.utils.KeycloakEventTestData;
import se.hrplatform.userservice.utils.UserProfileEntityTestData;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Import({JmsTestConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KeycloakUserEventListenerIT {

  @Autowired
  private JmsTemplate jmsTemplate;

  @Autowired
  private UserProfileRepository userProfileRepository;

  @Autowired
  private ObjectMapper objectMapper;


  private static final String TOPIC = "keycloak-user-event-topic";

  private void send(KeycloakUserEvent event) throws JsonProcessingException {
    jmsTemplate.convertAndSend(TOPIC, objectMapper.writeValueAsString(event));
  }

  @BeforeEach
  void clean() {
    userProfileRepository.deleteAll();
  }

  @Test
  void whenUserLoggedInEvent_andUserDoesNotExist_thenUserIsCreated() throws Exception {
    String userId = "login-1";

    send(KeycloakEventTestData.login(userId));

    await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> {
      assertTrue(userProfileRepository.existsById(userId));
    });
  }

  @Test
  void whenUserCreatedEvent_andUserDoesNotExist_thenUserIsSaved() throws Exception {
    String userId = "create-1";
    assertFalse(userProfileRepository.existsById(userId));

    send(KeycloakEventTestData.created(userId));

    await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> {
      assertTrue(userProfileRepository.existsById(userId));
    });
  }

  @Test
  void whenUserCreatedEvent_andUserAlreadyExists_thenSkipCreate() throws Exception {

    String userId = "user-id";
    UserProfileEntity alreadyExistingUser = userProfileRepository.save(
            UserProfileEntityTestData.createUserProfileEntity(userId));

    send(KeycloakEventTestData.created(userId));

    await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> {
      UserProfileEntity user = userProfileRepository.findById(userId).get();
      assertEquals(alreadyExistingUser.getUsername(), user.getUsername());
    });
  }

  @Test
  void whenUserUpdatedEvent_thenUserIsUpserted() throws Exception {
    String userId = "update-1";

    send(KeycloakEventTestData.updated(userId));

    await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> {
      UserProfileEntity user = userProfileRepository.findById(userId).orElseThrow();
      assertEquals("testuser_" + userId, user.getUsername());
    });
  }

  @Test
  void whenUserDeleted_thenUserIsRemoved() throws Exception {

    String userId = "user-id";
    userProfileRepository.save(UserProfileEntityTestData.createUserProfileEntity(userId));
    assertTrue(userProfileRepository.existsById(userId));

    send(KeycloakEventTestData.deleted(userId));

    await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> {
      assertFalse(userProfileRepository.existsById(userId));
    });
  }

  @Test
  void whenUnknownEventType_thenNoActionIsTaken() throws Exception {
    String userId = "unknown-1";

    KeycloakUserEvent event = KeycloakEventTestData.event("SOMETHING_ELSE", userId);

    send(event);

    Thread.sleep(300);
    assertFalse(userProfileRepository.existsById(userId));
  }

}

