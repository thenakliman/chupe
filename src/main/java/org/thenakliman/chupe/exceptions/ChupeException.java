package org.thenakliman.chupe.exceptions;

class ChupeException extends RuntimeException {
  ChupeException(String message) {
    super(message);
  }

  ChupeException(String message, Exception innerException) {
    super(message, innerException);
  }
}
