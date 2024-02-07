package edu.cnm.deepdive.codebreaker.model;

public class BadPoolException extends IllegalArgumentException {

  public BadPoolException() {
  }

  public BadPoolException(String message) {
    super(message);
  }

  public BadPoolException(String message, Throwable cause) {
    super(message, cause);
  }

  public BadPoolException(Throwable cause) {
    super(cause);
  }
}
