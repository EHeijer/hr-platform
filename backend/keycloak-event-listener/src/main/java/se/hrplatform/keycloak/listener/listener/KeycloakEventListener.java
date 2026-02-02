package se.hrplatform.keycloak.listener.listener;

import lombok.extern.jbosslog.JBossLog;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.OperationType;
import org.keycloak.events.admin.ResourceType;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.util.JsonSerialization;
import se.hrplatform.keycloak.listener.jms.JmsProducer;
import se.hrplatform.keycloak.listener.mapper.UserEventMapper;
import se.hrplatform.keycloak.listener.model.UserEvent;

@JBossLog
public class KeycloakEventListener implements EventListenerProvider {

    public static final String KEYCLOAK_USER_EVENT_TOPIC = "keycloak-user-event-topic";
    private final KeycloakSession session;

    public KeycloakEventListener(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public void onEvent(Event event) {
        log.info("## Incoming Event of type: " + event.getType());
        if(event.getType() == EventType.LOGIN) {
            handleUserEvent(event);
        }
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean includeRepresentation) {
        log.info("## Incoming AdminEvent of type: " + adminEvent.getOperationType());
        if (adminEvent.getResourceType() != ResourceType.USER) {
            return;
        }

        if (adminEvent.getOperationType() == OperationType.CREATE ||
                adminEvent.getOperationType() == OperationType.UPDATE ||
                adminEvent.getOperationType() == OperationType.DELETE) {

            handleUserAdminEvent(adminEvent);
        }
    }

    private void handleUserEvent(Event event) {

        try {
            UserModel user = session.users()
                    .getUserById(session.getContext().getRealm(), event.getUserId());
            if (user == null) return;

            UserEvent userEvent = UserEventMapper.mapToUserEvent(event, user);
            JmsProducer.send(JsonSerialization.writeValueAsString(userEvent), KEYCLOAK_USER_EVENT_TOPIC);

            log.infof("## USER EVENT SENT → %s (%s)",
                    userEvent.getType(), userEvent.getUsername());
        } catch (Exception e) {
            log.error("Failed to process event", e);
        }
    }

    private void handleUserAdminEvent(AdminEvent adminEvent) {
        try {
            String userId = extractUserId(adminEvent);
            RealmModel realm = session.realms().getRealm(adminEvent.getRealmId());
            UserModel user = session.users().getUserById(realm, userId);

            if (user == null && adminEvent.getOperationType() != OperationType.DELETE) {
                log.warn("User not found for admin event: " + userId);
                return;
            }

            UserEvent userEvent = UserEventMapper.mapToUserEvent(adminEvent, user);
            JmsProducer.send(JsonSerialization.writeValueAsString(userEvent), KEYCLOAK_USER_EVENT_TOPIC);

            log.infof("## USER EVENT SENT → %s (%s)",
                    userEvent.getType(), userEvent.getUsername());

        } catch (Exception e) {
            log.error("Failed to process admin event", e);
        }
    }

    private String extractUserId(AdminEvent event) {
        // users/{id}
        String path = event.getResourcePath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    @Override
    public void close() {}
}
