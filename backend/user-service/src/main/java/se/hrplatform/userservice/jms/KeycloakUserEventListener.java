package se.hrplatform.userservice.jms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import se.hrplatform.userservice.exception.UserEventProcessingException;
import se.hrplatform.userservice.mapper.UserMapper;
import se.hrplatform.userservice.model.dto.KeycloakUserEvent;
import se.hrplatform.userservice.model.entity.UserProfileEntity;
import se.hrplatform.userservice.repository.UserProfileRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class KeycloakUserEventListener {

    private final UserProfileRepository userProfileRepository;
    private final ObjectMapper objectMapper;

    @JmsListener(
        destination = "keycloak-user-event-topic",
        containerFactory = "topicListenerFactory",
        subscription = "user-service"
    )
    @Transactional
    public void onMessage(String message) {
        try {
            KeycloakUserEvent event =
                    objectMapper.readValue(message, KeycloakUserEvent.class);

            log.info("Received Keycloak event: type={}, userId={}",
                    event.type(), event.userId());

            switch (event.type()) {
                case "USER_LOGGED_IN" -> handleLogin(event);
                case "USER_CREATED" -> handleCreate(event);
                case "USER_UPDATED" -> handleUpdate(event);
                case "USER_DELETED" -> handleDelete(event);
                default -> {
                    log.warn("Unknown event type: {}", event.type());
                    throw new IllegalArgumentException("Incoming event type is unknown and can't be handled: " + event);
                }
            }

        } catch (Exception e) {
            throw new UserEventProcessingException("Failed to process Keycloak event", e); // gör att Artemis kan retry/DLQ
        }
    }

    private void handleLogin(KeycloakUserEvent event) {

        if (!userProfileRepository.existsById(event.userId())) {
            log.info("User profile with id '{}' has logged in, but doesn't exists in user table. Creating it..", event.userId());
            UserProfileEntity user = UserMapper.mapToUserProfileEntity(event);
            userProfileRepository.save(user);

            log.info("User profile created for user '{}'", user.getUsername());
        }

        log.debug("User profile already exists with id '{}'", event.userId());
    }

    private void handleCreate(KeycloakUserEvent event) {
        if (userProfileRepository.existsById(event.userId())) {
            log.info("User profile already exists, skipping create: {}", event.userId());
            return;
        }

        UserProfileEntity user = UserMapper.mapToUserProfileEntity(event);
        userProfileRepository.save(user);

        log.info("User profile created for user '{}'", user.getUsername());
    }

    private void handleUpdate(KeycloakUserEvent event) {
        UserProfileEntity user = UserMapper.mapToUserProfileEntity(event);
        userProfileRepository.save(user); // UPSERT

        log.info("User profile updated: {}", user);
    }

    private void handleDelete(KeycloakUserEvent event) {
        userProfileRepository.deleteById(event.userId());
        log.info("User profile deleted for user with id '{}'", event.userId());
    }
}

