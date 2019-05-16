package org.thenakliman.chupe.exceptions;

public class BadRequestException extends ChupeException {
  public BadRequestException(String message) {
    super(message);
  }

  public BadRequestException(String message, Exception innerException) {
    super(message, innerException);
  }
}
