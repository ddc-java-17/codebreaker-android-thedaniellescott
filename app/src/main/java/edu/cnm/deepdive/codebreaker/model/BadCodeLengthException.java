package edu.cnm.deepdive.codebreaker.model;

@SuppressWarnings("unused")
public class BadCodeLengthException extends IllegalArgumentException {

  public BadCodeLengthException() {
  }

  public BadCodeLengthException(String message) {
    super(message);
  }

  public BadCodeLengthException(String message, Throwable cause) {
    super(message, cause);
  }

  public BadCodeLengthException(Throwable cause) {
    super(cause);
  }
}
