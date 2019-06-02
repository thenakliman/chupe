package org.thenakliman.chupe.exceptions;

public class AccessDeniedException extends ChupeException {
  public AccessDeniedException(String message) {
    super(message);
  }

  public AccessDeniedException(String message, Exception innerException) {
    super(message, innerException);
  }
}