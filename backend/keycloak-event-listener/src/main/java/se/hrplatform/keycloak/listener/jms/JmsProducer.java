package se.hrplatform.keycloak.listener.jms;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import jakarta.jms.*;

public class JmsProducer {

    private static final String BROKER_URL = "tcp://artemis:61616";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";

    private static final ActiveMQConnectionFactory factory =
            new ActiveMQConnectionFactory(BROKER_URL, USERNAME, PASSWORD);

    public static void send(String json, String topicName) {
        try (Connection connection = factory.createConnection()) {
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(topicName);
            MessageProducer producer = session.createProducer(topic);
            TextMessage message = session.createTextMessage(json);
            producer.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
