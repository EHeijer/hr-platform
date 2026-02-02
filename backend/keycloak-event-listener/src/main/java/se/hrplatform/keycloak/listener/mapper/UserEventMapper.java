package se.hrplatform.keycloak.listener.mapper;

import org.keycloak.events.Event;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.UserModel;
import se.hrplatform.keycloak.listener.model.UserEvent;

public class UserEventMapper {

    public static UserEvent mapToUserEvent(Event event, UserModel user) {

        UserEvent.Type type = switch (event.getType()) {
            case LOGIN -> UserEvent.Type.USER_LOGGED_IN;
            default -> throw new IllegalArgumentException("Unexpected event type: " + event.getType());
        };

        UserEvent userEvent = new UserEvent();
        userEvent.setType(type);
        userEvent.setUserId(user.getId());
        userEvent.setTimestamp(System.currentTimeMillis());
        userEvent.setUsername(user.getUsername());
        userEvent.setName(user.getFirstName(), user.getLastName());
        userEvent.setEmail(user.getEmail());
        userEvent.setEnabled(user.isEnabled());

        return userEvent;
    }

    public static UserEvent mapToUserEvent(AdminEvent adminEvent,
                                           UserModel user) {

        UserEvent.Type type = switch (adminEvent.getOperationType()) {
            case CREATE -> UserEvent.Type.USER_CREATED;
            case UPDATE -> UserEvent.Type.USER_UPDATED;
            case DELETE -> UserEvent.Type.USER_DELETED;
            default -> throw new IllegalArgumentException("Unexpected event type: " + adminEvent.getOperationType());
        };

        UserEvent userEvent = new UserEvent();
        userEvent.setType(type);
        userEvent.setUserId(user.getId());
        userEvent.setTimestamp(System.currentTimeMillis());
        userEvent.setUsername(user.getUsername());
        userEvent.setName(user.getFirstName(), user.getLastName());
        userEvent.setEmail(user.getEmail());
        userEvent.setEnabled(user.isEnabled());

        return userEvent;
    }
}
