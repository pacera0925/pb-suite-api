package com.paulcera.pb_suite_api.security.controller;

import com.paulcera.pb_suite_api.security.dto.ResponseMessage;
import com.paulcera.pb_suite_api.security.exception.AlreadyLoggedInException;
import com.paulcera.pb_suite_api.security.exception.InvalidRefreshTokenException;
import com.paulcera.pb_suite_api.security.exception.TokenNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = AuthenticationController.class)
public class AuthenticationControllerAdvice {

    @ExceptionHandler({BadCredentialsException.class, AccessDeniedException.class})
    public ResponseEntity<ResponseMessage> handleUnauthorizedException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseMessage(ex.getMessage()));
    }

    @ExceptionHandler(AlreadyLoggedInException.class)
    public ResponseEntity<ResponseMessage> handleAlreadyLoggedInException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseMessage(ex.getMessage()));
    }

    @ExceptionHandler({TokenNotFoundException.class, InvalidRefreshTokenException.class})
    public ResponseEntity<ResponseMessage> handleTokenNotFoundException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseMessage> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("An unexpected error occurred."));
    }

}
