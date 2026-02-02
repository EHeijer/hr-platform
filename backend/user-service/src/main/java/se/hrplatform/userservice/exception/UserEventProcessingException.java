package se.hrplatform.userservice.exception;

public class UserEventProcessingException extends RuntimeException {
  public UserEventProcessingException(String message, Throwable cause) {
    super(message, cause);
  }
}

