package edu.cnm.deepdive.codebreaker.model;

public class BadGuessLengthException extends IllegalArgumentException {

  public BadGuessLengthException() {

  }

  public BadGuessLengthException(String message) {
    super(message);
  }

  public BadGuessLengthException(String message, Throwable cause) {
    super(message, cause);
  }

  public BadGuessLengthException(Throwable cause) {
    super(cause);
  }
}
