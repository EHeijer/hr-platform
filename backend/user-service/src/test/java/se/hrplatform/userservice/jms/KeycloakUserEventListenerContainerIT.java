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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import se.hrplatform.userservice.config.JmsTestConfig;
import se.hrplatform.userservice.model.dto.KeycloakUserEvent;
import se.hrplatform.userservice.model.entity.UserProfileEntity;
import se.hrplatform.userservice.repository.UserProfileRepository;
import se.hrplatform.userservice.utils.KeycloakEventTestData;
import se.hrplatform.userservice.utils.UserProfileEntityTestData;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import({JmsTestConfig.class})
class KeycloakUserEventListenerContainerIT {

  @Autowired
  private JmsTemplate jmsTemplate;

  @Autowired
  private UserProfileRepository userProfileRepository;

  @Autowired
  private ObjectMapper objectMapper;

  private static final String TOPIC = "keycloak-user-event-topic";
  private static final String ARTEMIS_USERNAME = "artemis";
  private static final String ARTEMIS_PASSWORD = "artemis";

  static PostgreSQLContainer<?> postgres =
          new PostgreSQLContainer<>("postgres:16")
                  .withDatabaseName("testdb")
                  .withUsername("test")
                  .withPassword("test");

  static GenericContainer<?> artemis =
          new GenericContainer<>("apache/activemq-artemis:latest")
                  .withExposedPorts(61616)
                  .withEnv("ARTEMIS_USERNAME", ARTEMIS_USERNAME)
                  .withEnv("ARTEMIS_PASSWORD", ARTEMIS_PASSWORD);

  static {
    postgres.start();
    artemis.start();
  }

  @DynamicPropertySource
  static void registerProps(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
    registry.add("spring.flyway.enabled", () -> false);
    registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    registry.add("spring.datasource.driverClassName", postgres::getDriverClassName);
    registry.add("spring.jpa.show-sql", () -> false);


    registry.add("spring.artemis.broker-url",
            () -> "tcp://" + artemis.getHost() + ":" + artemis.getMappedPort(61616));
    registry.add("spring.artemis.user", () -> ARTEMIS_USERNAME);
    registry.add("spring.artemis.password", () -> ARTEMIS_PASSWORD);
    registry.add("spring.artemis.mode", () -> "native");
  }

  private void send(KeycloakUserEvent event) throws JsonProcessingException {
    jmsTemplate.convertAndSend(TOPIC, objectMapper.writeValueAsString(event));
  }

  @BeforeEach
  void setup() {
    userProfileRepository.deleteAll();
  }

  @Test
  void whenUserLoggedIn_andUserDoesNotExist_thenUserIsCreated() throws Exception {
    String userId = "login-1";

    send(KeycloakEventTestData.login(userId));

    await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> {
      assertTrue(userProfileRepository.existsById(userId));
    });
  }

  @Test
  void whenUserCreated_andUserDoesNotExist_thenUserIsSaved() throws Exception {
    String userId = "create-1";

    send(KeycloakEventTestData.created(userId));

    await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> {
      assertTrue(userProfileRepository.existsById(userId));
    });
  }

  @Test
  void whenUserCreated_andUserAlreadyExists_thenSkipCreate() throws Exception {

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
  void whenUserUpdated_thenUserIsUpserted() throws Exception {
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

    Thread.sleep(2000);
    assertFalse(userProfileRepository.existsById(userId));
  }
}
