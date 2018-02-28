package com.ivan.sub.miniroulette.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created on 2/28/18.
 */
@ControllerAdvice
public class CommonExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(CommonExceptionHandler.class);

  @ExceptionHandler(Throwable.class)
  public ResponseEntity<Object> handleError(Throwable e) {
    logger.error(e.getMessage(), e);
    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
