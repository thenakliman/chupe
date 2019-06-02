package org.thenakliman.chupe.exceptions;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<String> handleException(NotFoundException exception) {
    logger.error(exception.getMessage(), exception);
    return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<String> handleException(BadRequestException exception) {
    logger.error(exception.getMessage(), exception);
    return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<String> handleException(AccessDeniedException exception) {
    logger.error(exception.getMessage(), exception);
    return new ResponseEntity<>(exception.getMessage(), HttpStatus.FORBIDDEN);
  }
}
