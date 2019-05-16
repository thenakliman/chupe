package org.thenakliman.chupe.exceptions;

public class NotFoundException extends ChupeException {
  public NotFoundException(String message) {
    super(message);
  }

  public NotFoundException(String message, Exception innerException) {
    super(message, innerException);
  }
}
