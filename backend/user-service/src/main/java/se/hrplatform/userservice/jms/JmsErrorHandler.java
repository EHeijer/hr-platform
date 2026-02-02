package se.hrplatform.userservice.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

@Component
@Slf4j
public class JmsErrorHandler implements ErrorHandler {

    @Override
    public void handleError(Throwable t) {
        log.error("JMS listener error, message will be retried or sent to DLQ", t);
        // ❗ Viktigt: throw INTE här – Spring sköter rollback
    }
}
