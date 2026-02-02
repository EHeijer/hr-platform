package se.hrplatform.userservice.jms;

import jakarta.jms.ConnectionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.JmsTransactionManager;

@Configuration
public class JmsConfig {

    @Bean
    public DefaultJmsListenerContainerFactory topicListenerFactory(
            ConnectionFactory connectionFactory,
            JmsErrorHandler jmsErrorHandler) {

        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(true);
        factory.setSubscriptionDurable(true);
        factory.setSubscriptionShared(true);
        factory.setSessionTransacted(true);
        factory.setTransactionManager(new JmsTransactionManager(connectionFactory));
        factory.setErrorHandler(jmsErrorHandler);

        return factory;
    }

}

