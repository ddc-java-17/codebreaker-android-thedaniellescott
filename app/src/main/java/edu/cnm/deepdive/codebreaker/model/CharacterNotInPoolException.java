package edu.cnm.deepdive.codebreaker.model;

public class CharacterNotInPoolException extends IllegalArgumentException {

  public CharacterNotInPoolException() {
  }

  public CharacterNotInPoolException(String message) {
    super(message);
  }

  public CharacterNotInPoolException(String message, Throwable cause) {
    super(message, cause);
  }

  public CharacterNotInPoolException(Throwable cause) {
    super(cause);
  }
}
